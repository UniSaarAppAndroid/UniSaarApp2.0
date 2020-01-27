package com.example.uniapp_20.SBActivities;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.util.Util;
import com.example.uniapp_20.BuildConfig;
import com.example.uniapp_20.R;


public class AboutActivity extends AppCompatActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();

    /*
     * Will be called when activity created first time e.g. from scratch
     * */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_us);

        Button contact = (Button) findViewById(R.id.contact_btn);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject) );
                String emailBody = "";
                sendIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                sendIntent.setType("message/rfc822");
                AboutActivity.this.startActivity(Intent.createChooser(sendIntent, "Send Email"));
            }
        });

        Button githubBtn = (Button) findViewById(R.id.github_btn);
        githubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}
