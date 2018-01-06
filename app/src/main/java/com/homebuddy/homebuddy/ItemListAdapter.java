package com.homebuddy.homebuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>{
    private List<ItemListModel> activityList;
    private Context mContext;
    private static final String TAG = "AddToCart";
    private ProgressDialog pDialog;

    ItemListAdapter(List<ItemListModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName , itemPrice , itemBrand ;
        ImageView itemImage ;
        public Button addToCart;

        public ViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemPrice = (TextView) view.findViewById(R.id.item_price);
            itemBrand = (TextView) view.findViewById(R.id.item_brand);
            addToCart = (Button) view.findViewById(R.id.add_to_cart);
            itemImage = (ImageView) view.findViewById(R.id.item_icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                      Toast.makeText(mContext,"Tap on Add to cart",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_model, parent, false);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return new ItemListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemListAdapter.ViewHolder holder, int position) {
        ItemListModel activityListItems = activityList.get(position);
        holder.itemName.setText(activityListItems.getItemName());
        holder.itemPrice.setText(activityListItems.getItemPrice());
        holder.itemBrand.setText(activityListItems.getItemBrand());


        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View promptsView;
                TextView textView;
                final EditText editText;
                LayoutInflater lil = LayoutInflater.from(mContext);
                promptsView = lil.inflate(R.layout.amount_select_window, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setView(promptsView);

                textView = (TextView)promptsView.findViewById(R.id.item_name);
                editText = (EditText)promptsView.findViewById(R.id.amount);

                textView.setText(holder.itemName.getText().toString());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AddToCartApiCall("1" , holder.itemName.getText().toString() , editText.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    private void AddToCartApiCall(String id, String itemName, String amount){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String api = "http://192.168.43.43:8000/addCart/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );
        data.put( "item_name", itemName );
        data.put( "item_amount", amount );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    Toast.makeText(mContext,status,Toast.LENGTH_LONG).show();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
                hideProgressDialog();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(request);

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
