package com.homebuddy.homebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class SignIn extends AppCompatActivity {
    EditText phone , name , address , password ;
    Button button;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    private static final String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        linearLayout = (LinearLayout)findViewById(R.id.signIn_form);
        progressBar = (ProgressBar)findViewById(R.id.signIn_progress);

        name = (EditText)findViewById(R.id.signInName);
        phone = (EditText)findViewById(R.id.signInPhone);
        password = (EditText)findViewById(R.id.signInPassword);
        address = (EditText)findViewById(R.id.signInAddress);
        button = (Button)findViewById(R.id.signIn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num1 = name.getText().toString();
                String num2 = phone.getText().toString();
                String num3 = password.getText().toString();
                String num4 = address.getText().toString();

                if (!num1.equals("") && !num2.equals("") &&!num3.equals("")){
                    SignInApiCall(num1 , num2 , num3 , num4);
                }
                else
                    Toast.makeText(SignIn.this,"Please enter all details.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    void SignInApiCall( String name , String phone , String password , String address){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        String api = "http://192.168.43.43:8000/register/";
        Map<String, Object> data = new HashMap<>();
        data.put( "name", name );
        data.put( "phone", phone );
        data.put( "password", password );
        data.put( "address", address );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("message");
                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
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
