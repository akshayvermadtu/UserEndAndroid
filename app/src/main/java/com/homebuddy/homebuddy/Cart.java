package com.homebuddy.homebuddy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cart extends Fragment {
    protected View mView;
    ProgressBar progressBar;
    private List<CartModel> activityList = new ArrayList<>();
    private CartAdapter mAdapter;
    CartModel activityItems;
    LinearLayoutManager layoutManager ;
    RecyclerView recyclerView ;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    TextView textView, netAmount , deliveryCharge ;
    Button button;
    UserSessionManager session;
    String user_id;

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        this.mView = view;
        setHasOptionsMenu(true);

        ((Home) getActivity())
                .setActionBarTitle("My Cart");

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        textView = (TextView)mView.findViewById(R.id.empty_cart);
        netAmount = (TextView)mView.findViewById(R.id.total);
        deliveryCharge = (TextView)mView.findViewById(R.id.delivery_charges);

        session = new UserSessionManager(getActivity());
        final HashMap<String , String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.USER_ID);

        button = (Button)mView.findViewById(R.id.place_order_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View promptsView;
                RadioGroup radioGroup;
                final TextView textView;
                LayoutInflater lil = LayoutInflater.from(getActivity());
                promptsView = lil.inflate(R.layout.payment_mode_window, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);

                radioGroup = (RadioGroup) promptsView.findViewById(R.id.payment_options);
                textView = (TextView)promptsView.findViewById(R.id.payment_type_display);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if(checkedId == R.id.payment_options_cod) {
                            textView.setText("cod");
                        }

                        else if (checkedId == R.id.payment_options_pod){
                            textView.setText("pod");
                        }
                    }
                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (!Objects.equals(textView.getText().toString(), "none")){
                                            PlaceOrderApiCall(user_id , textView.getText().toString());
                                        }
                                        else
                                            Toast.makeText(getActivity(),"Please select payment type",Toast.LENGTH_SHORT).show();

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

        progressBar = (ProgressBar)mView.findViewById(R.id.cart_progress_bar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.cart_recycler);
        mAdapter = new CartAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ShowCartApiCall();
        BillApiCall(user_id);

        return view;
    }

    private void ShowCartApiCall(){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/showCart/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, api,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        if (!response.equals("\"{}\"")){
                            List items;
                            String convertString = response.replace("\"", "").replaceAll("[\\[\\](){}]","");
                            String str[] = convertString.split(", ");
                            items = Arrays.asList(str);
                            for(Object s: items){
                                String str2[] = s.toString().split(":");
                                String key = str2[0].replace("\'", "");
                                String value = str2[1].replace("\'", "");
                                activityItems = new CartModel(key , value);
                                activityList.add(activityItems);
                            }

                            mAdapter = new CartAdapter(activityList, getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            hideProgress();
                        }

                        else{
                            textView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id", user_id);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void PlaceOrderApiCall(String id , String paymentType){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/placeOrder/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );
        data.put( "payment_type", paymentType );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        String bill = response.getString("bill");
                        Intent intent = new Intent("com.homebuddy.homebuddy.OrderSuccess");
                        intent.putExtra("bill",bill);
                        startActivity(intent);
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
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


    private void BillApiCall(String id){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/bill/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String amount = response.get("bill").toString();
                    netAmount.setText("Rs. "+amount);
                    if(!amount.equals("0")){
                        if((Double)response.get("bill")>200)
                            deliveryCharge.setText("Rs. 0");
                        else
                            deliveryCharge.setText("Rs. 20");
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
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

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {

                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }

                    return true;
                }
                return false;
            }
        });

    }

}
