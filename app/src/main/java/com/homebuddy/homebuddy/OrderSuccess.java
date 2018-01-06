package com.homebuddy.homebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        TextView textView;
        Button button ;

        textView = (TextView)findViewById(R.id.bill_amount);
        String bill = getIntent().getExtras().getString("bill");
        textView.setText("Bill amount : Rs " + bill);

        button = (Button)findViewById(R.id.continue_shop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.homebuddy.homebuddy.Home");
                startActivity(intent);
            }
        });

    }
}
