package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.uniapp_20.R;
import com.example.uniapp_20.model.News;
import com.example.uniapp_20.utils.FrontEngine;

public class NewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView newsDate, newsName, newsDescription;
    ImageView newsThumbnail;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
//        News news = (News) getIntent().getSerializableExtra("news");
        //Toast.makeText(this, news.getName(), Toast.LENGTH_LONG).show();
        toolbar = findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("News");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*newsDate = findViewById(R.id.news_date);
        newsName = findViewById(R.id.news_name);
        newsDescription = findViewById(R.id.news_description);
        newsThumbnail = findViewById(R.id.news_thumbnail);
        newsDate.setText(news.getDate());
        newsName.setText(news.getName());
        newsDescription.setText(news.getDescription());
        if (!news.getThumbnail().equals("")) {
            Glide.with(this).load(news.getThumbnail()).into(newsThumbnail);
        }
        else {
            newsThumbnail.setVisibility(View.GONE);
        }*/

        webView = (WebView) findViewById(R.id.news_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(FrontEngine.getInstance().base_url + "news/details?id=" + getIntent().getStringExtra("news"));

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
