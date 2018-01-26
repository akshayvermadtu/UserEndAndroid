package com.homebuddy.homebuddy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrders extends Fragment {
    protected View mView;
    private static final String TAG = "myOrders";
    RecyclerView recyclerView ;
    private List<MyOrderModel> activityList = new ArrayList<>();
    private MyOrderAdapter mAdapter;
    MyOrderModel activityItems;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager ;
    UserSessionManager session;
    String user_id;

    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        this.mView = view;

        ((Home) getActivity())
                .setActionBarTitle("My Orders");

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar)mView.findViewById(R.id.my_order_progress_bar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.my_order_recycler);
        mAdapter = new MyOrderAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        session = new UserSessionManager(getActivity());
        final HashMap<String , String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.USER_ID);

        MyOrderListApiCall(user_id);

        return view ;
    }

    void MyOrderListApiCall(String id){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/myOrders/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );

        VolleyRequester request = new VolleyRequester(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = jsonArray.length(); i >=0; i--) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);
                        String itemList = itemDetails.get("item_list").toString();
                        String bill = itemDetails.get("amount").toString();
                        String status = itemDetails.get("status").toString();
                        String payment = itemDetails.get("delivery_type").toString();
                        String date_time = itemDetails.get("order_time").toString();
                        String str[] = date_time.split("T");
                        String date  = str[0];
                        String rawTime = str[1];
                        String strTime[] = rawTime.split(":");
                        String hours = strTime[0];
                        String minutes = strTime[1];
                        String time = hours + ":" + minutes ;

                        String finalDateTime = date + " " + time ;

                        activityItems = new MyOrderModel(itemList , "₹" + bill ,"Status : " +  status ,"Payment:" + payment , finalDateTime);
                        activityList.add(activityItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter = new MyOrderAdapter(activityList, getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                hideProgress();
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
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,fragment)
                                .addToBackStack(null)
                                .commit();

                    }

                    return true;
                }
                return false;
            }
        });

    }



}
