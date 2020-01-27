package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.uniapp_20.R;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

public class NewsFeedSettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    public static String CURRENT_TAG = MainActivity.TAG_SETTINGS;
    private boolean val = false;

    Switch switchDE,switchFR,switchEN, switch1, switch2, switch3, switch4, switch5, switch6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_setting);

        toolbar = findViewById(R.id.news_feed_setting_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        switchDE = findViewById(R.id.sw_de);
        switchFR = findViewById(R.id.sw_fr);
        switchEN = findViewById(R.id.sw_en);
        switch1 = findViewById(R.id.sw_veranstaltungen);
        switch2 = findViewById(R.id.sw_veranstaltungen_fur_studieninteressierte);
        switch3 = findViewById(R.id.sw_news);
        switch4 = findViewById(R.id.sw_forschung);
        switch5 = findViewById(R.id.sw_startseite);
        switch6 = findViewById(R.id.sw_schuler);

        if(SessionManager.getInstance(getApplicationContext()).switchDE()){
            switchDE.setChecked(true);
        }
        else{
            switchDE.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchFR()){
            switchFR.setChecked(true);
        }
        else{
            switchFR.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchEN()){
            switchEN.setChecked(true);
        }
        else{
            switchEN.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchOne()){
            switch1.setChecked(true);
        }
        else {
            switch1.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchTwo()){
            switch2.setChecked(true);
        }
        else {
            switch2.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchThree()){
            switch3.setChecked(true);
        }
        else {
            switch3.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchFour()){
            switch4.setChecked(true);
        }
        else {
            switch4.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchFive()){
            switch5.setChecked(true);
        }
        else {
            switch5.setChecked(false);
        }

        if(SessionManager.getInstance(getApplicationContext()).switchSix()){
            switch6.setChecked(true);
        }
        else {
            switch6.setChecked(false);
        }
        switchDE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchDE.isChecked()){
                    FrontEngine.getInstance().language = "de";
                    SessionManager.getInstance(getApplicationContext()).setSwitchDE(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchDE(false);
                }
            }
        });
        switchFR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(switchFR.isChecked()){
                            FrontEngine.getInstance().language = "fr";
                            SessionManager.getInstance(getApplicationContext()).setSwitchFR(true);
                        }
                        else{
                            SessionManager.getInstance(getApplicationContext()).setSwitchFR(false);
                        }
                    }
                });
        switchEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(switchEN.isChecked()){
                                    FrontEngine.getInstance().language = "en";
                                    SessionManager.getInstance(getApplicationContext()).setSwitchEN(true);
                                }
                                else{
                                    SessionManager.getInstance(getApplicationContext()).setSwitchEN(false);
                                }
                            }
                        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch1.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchOne(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchOne(false);
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch2.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchTwo(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchTwo(false);
                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch3.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchThree(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchThree(false);
                }
            }
        });

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch4.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchFour(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchFour(false);
                }
            }
        });

        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch5.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchFive(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchFive(false);
                }
            }
        });

        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch6.isChecked()){
                    SessionManager.getInstance(getApplicationContext()).setSwitchSix(true);
                }
                else{
                    SessionManager.getInstance(getApplicationContext()).setSwitchSix(false);
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish(); // close this activity and return to preview activity (if there is any)
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent mainActivity = new Intent(NewsFeedSettingActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

}
