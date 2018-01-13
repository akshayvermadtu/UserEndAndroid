package com.homebuddy.homebuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.Objects;

class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private List<CartModel> activityList;
    private Context mContext;
    private static final String TAG = "RemoveFromCart";
    private ProgressDialog pDialog;
    private String user_id;

    CartAdapter(List<CartModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName , itemQuantity ;
        ImageButton removeButton ;

        ViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemQuantity = (TextView) view.findViewById(R.id.item_amount);
            removeButton = (ImageButton) view.findViewById(R.id.remove_item);
        }
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_model, parent, false);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        UserSessionManager session = new UserSessionManager(mContext);
        final HashMap<String , String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.USER_ID);
        return new CartAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, int position) {
        CartModel activityListItems = activityList.get(position);
        holder.itemName.setText(activityListItems.getItemName());
        holder.itemQuantity.setText(activityListItems.getItemQuantity());
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Do you want to remove this item from your cart ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RemoveFromCartApiCall(user_id , holder.itemName.getText().toString());
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    private void RemoveFromCartApiCall(String id, String itemName){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String api = "https://homebuddy2018.herokuapp.com/removeCart/";
        Map<String, Object> data = new HashMap<>();
        data.put( "customer_id", id );
        data.put( "item_name", itemName );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");

                    if (Objects.equals(status, "success")){
                        Toast.makeText(mContext,"Removed from your cart",Toast.LENGTH_LONG).show();
                        Cart fragment = new Cart();
                        ((Home) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                    else
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

