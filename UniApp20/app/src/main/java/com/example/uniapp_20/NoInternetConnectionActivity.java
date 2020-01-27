package com.example.uniapp_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uniapp_20.SBActivities.MainActivity;

public class NoInternetConnectionActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        String activity = getIntent().getStringExtra("Activity");
        btn = findViewById(R.id.btn_no_internet);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equals("SplashActivity")){
                    Intent mainActivity = new Intent(NoInternetConnectionActivity.this, SplashScreen.class);
                    startActivity(mainActivity);
                    finish();
                }
                else if(activity.equals("MainActivity")){
                    Intent mainActivity = new Intent(NoInternetConnectionActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            }
        });
    }
}
