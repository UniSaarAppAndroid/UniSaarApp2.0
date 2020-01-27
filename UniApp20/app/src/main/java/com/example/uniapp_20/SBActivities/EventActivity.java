package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uniapp_20.R;
import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.model.News;
import com.example.uniapp_20.utils.FrontEngine;

public class EventActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView eventDate, eventName, eventDescription;
    ImageView eventThumbnail;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
//        Event event = (Event) getIntent().getSerializableExtra("event");
        //Toast.makeText(this, event.getName(), Toast.LENGTH_LONG).show();
        toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        /*eventDate = findViewById(R.id.event_date);
        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        eventDate.setText(event.getHappeningDate());
        eventName.setText(event.getHeadline());
        eventDescription.setText(event.getLocation());*/
        webView = findViewById(R.id.event_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(FrontEngine.getInstance().base_url + "events/details?id=" + getIntent().getStringExtra("event"));


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
        super.onBackPressed();
    }

}
