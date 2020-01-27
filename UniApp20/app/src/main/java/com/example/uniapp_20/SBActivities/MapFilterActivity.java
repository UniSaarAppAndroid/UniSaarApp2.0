package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.uniapp_20.R;
import com.example.uniapp_20.utils.SessionManager;

public class MapFilterActivity extends AppCompatActivity {

    Toolbar toolbar;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);

        toolbar = findViewById(R.id.map_filter_toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        radioGroup = findViewById(R.id.map_radio_group);

        if(SessionManager.getInstance(getApplicationContext()).isSB()){
            radioGroup.check(R.id.radio_saar);
        }
        else{
            radioGroup.check(R.id.radio_homburg);
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_saar:
                if (checked)
                    SessionManager.getInstance(getApplicationContext()).setHB(false);
                    if(SessionManager.getInstance(getApplicationContext()).isSB()){
                        break;
                    }
                    else{
                        SessionManager.getInstance(getApplicationContext()).setSB(true);
                        Intent mainActivity = new Intent(MapFilterActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                        break;
                    }

            case R.id.radio_homburg:
                if (checked) {
                    SessionManager.getInstance(getApplicationContext()).setSB(false);
                    if (SessionManager.getInstance(getApplicationContext()).isHB()) {
                        break;
                    } else {
                        SessionManager.getInstance(getApplicationContext()).setHB(true);
                        Intent mainActivity = new Intent(MapFilterActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                        break;
                    }
                }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        Intent mainActivity = new Intent(MapFilterActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();

    }
}
