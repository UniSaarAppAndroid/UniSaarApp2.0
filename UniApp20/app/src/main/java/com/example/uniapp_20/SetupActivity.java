package com.example.uniapp_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uniapp_20.SBActivities.MainActivity;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

import java.util.Locale;

public class SetupActivity extends AppCompatActivity {

    Button saarbrueckenBtn, homburgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String lan = Locale.getDefault().getDisplayLanguage();
        if (lan.equals("Deutsch")) {
            FrontEngine.getInstance().language = "de";
            setContentView(R.layout.activity_setup_ger);
        } else {
            FrontEngine.getInstance().language = "en";
            setContentView(R.layout.activity_setup);
        }
        saarbrueckenBtn = findViewById(R.id.btn_saarbruecken);

        homburgBtn = findViewById(R.id.btn_homburg);

        saarbrueckenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrontEngine.getInstance().isSB = true;
                SessionManager.getInstance(getBaseContext()).setSB(true);
                Intent mainActivity = new Intent(SetupActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        homburgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FrontEngine.getInstance().isHomburg = true;
                SessionManager.getInstance(getBaseContext()).setHB(true);
                Intent mainActivity = new Intent(SetupActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

    }
}
