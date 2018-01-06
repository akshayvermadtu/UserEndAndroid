package com.homebuddy.homebuddy;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    Boolean connectionCheck;
    private static final String TAG = "Splash";
    UserSessionManager session;
    String token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_splash_screen);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connectionCheck = networkInfo != null && networkInfo.isConnected();

        session = new UserSessionManager(getApplicationContext());
//        final Boolean check = session.isUserLoggedIn();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                HashMap<String , String> user = session.getUserDetails();
//                token = user.get(UserSessionManager.TOKEN);

                Intent intent = new Intent("com.homebuddy.homebuddy.Home");
                startActivity(intent);
//
//                    if (connectionCheck) {
//                        Toast.makeText(getApplicationContext(),"Net connection available",Toast.LENGTH_SHORT).show();
//
//                    }
//                    else
//                        Toast.makeText(getApplicationContext(),"Net connection not available",Toast.LENGTH_SHORT).show();

            }
        }, 3000);

    }

}
