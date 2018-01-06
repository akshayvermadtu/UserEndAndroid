package com.homebuddy.homebuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Map;


public class UserDetails extends Fragment {
    protected View mView;
    private static final String TAG = "PlaceOrder";
    LinearLayout linearLayout ;
    ProgressBar progressBar ;
    TextView nameText , phoneText , addressText ;


    public UserDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        this.mView = view;
        setHasOptionsMenu(true);

        linearLayout = (LinearLayout)mView.findViewById(R.id.user_details);
        progressBar = (ProgressBar) mView.findViewById(R.id.my_details_progress_bar);
        nameText = (TextView)mView.findViewById(R.id.user_name);
        phoneText = (TextView)mView.findViewById(R.id.user_phone);
        addressText = (TextView)mView.findViewById(R.id.user_address);

        ((Home) getActivity())
                .setActionBarTitle("My details");

        UserDetailsApiCall("1");

        return view ;
    }

    private void UserDetailsApiCall(String id){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "http://192.168.43.43:8000/myDetails/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String name = response.getString("name");
                    nameText.setText(name);
                    String phone = response.getString("phone");
                    phoneText.setText(phone);
                    String address = response.getString("address");
                    addressText.setText(address);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
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
        linearLayout.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }

}