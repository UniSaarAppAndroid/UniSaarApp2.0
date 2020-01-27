package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.R;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class DirectoryActivity extends AppCompatActivity {

    String number = "";
    TextView title, name, email, street, city, building, office, office_hours, phone, fax, website;
    ImageButton call, emailBtn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        toolbar = findViewById(R.id.directory_toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        title = findViewById(R.id.directory_title);
        name = findViewById(R.id.directory_name);
        email = findViewById(R.id.directory_email);
        street = findViewById(R.id.directory_street);
        city = findViewById(R.id.directory_city);
        building = findViewById(R.id.directory_building);
        office = findViewById(R.id.directory_office);
        office_hours = findViewById(R.id.directory_office_hours);
        phone = findViewById(R.id.directory_phone);
        fax = findViewById(R.id.directory_fax);
        website = findViewById(R.id.directory_website);
        call = findViewById(R.id.directory_call);
        emailBtn = findViewById(R.id.btn_directory_email);

        if(getIntent().getStringExtra("helpfullnumbers") != null){
            String[] data = getIntent().getStringExtra("helpfullnumbers").split(":");
            name.setText(data[0]);
            phone.setText(data[1]);
            call.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            emailBtn.setVisibility(View.GONE);
            website.setVisibility(View.GONE);
            fax.setVisibility(View.GONE);
            office_hours.setVisibility(View.GONE);
            office.setVisibility(View.GONE);
            street.setVisibility(View.GONE);
            city.setVisibility(View.GONE);
            building.setVisibility(View.GONE);
        }
        else if (getIntent().getStringExtra("directory") != null) {

            String id = getIntent().getStringExtra("directory");
            loadDirectoryData(id);
        }
        building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance(getBaseContext()).setScreen("MapFragment");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email.getText().toString()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                //intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
                //intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email.getText().toString()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                //intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
                //intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website.getText().toString()));
                startActivity(browserIntent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLocationPermission()){
                    if (ContextCompat.checkSelfPermission(DirectoryActivity.this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (!phone.getText().toString().equals("")) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            /*String[] number = new String[1];
                            String[] secondPart = new String[1];
                            String num= "";
                            if(phone.getText().toString().contains("/")) {
                                number = phone.getText().toString().split("/");
                            }
                            if(number[1].contains("-")) {
                                secondPart = number[1].split("-");
                            }
                            if(number[0].contains("\\(")) {
                               num = number[0].split("\\(")[0] + number[0].split("\\)")[1] + secondPart[0] + secondPart[1];
                            }
                            num = num.replace(" ", "").split(":")[1];*/
                            String number = phone.getText().toString();
                            if(number.contains("(0)")){
                                number = number.replace("(0)", "");
                            }
                            if(number.contains("/")){
                                number = number.replace("/", "");
                            }
                            if(number.contains("-")){
                                number = number.replace("-", "");
                            }
                            if(number.contains(" ")){
                                number = number.replace(" ", "");
                            }
                            if(number.contains(":")) {
                                intent.setData(Uri.parse("tel:" + number.split(":")[1].trim()));
                            }
                            else {
                                intent.setData(Uri.parse("tel:" + number.trim()));
                            }
                            startActivity(intent);
                        }
                    }
                }

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLocationPermission()){
                    if (ContextCompat.checkSelfPermission(DirectoryActivity.this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (!phone.getText().toString().equals("")) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            /*String[] number = new String[1];
                            String[] secondPart = new String[1];
                            String num= "";
                            if(phone.getText().toString().contains("/")) {
                                number = phone.getText().toString().split("/");
                            }
                            if(number[1].contains("-")) {
                                secondPart = number[1].split("-");
                            }
                            if(number[0].contains("\\(")) {
                               num = number[0].split("\\(")[0] + number[0].split("\\)")[1] + secondPart[0] + secondPart[1];
                            }
                            num = num.replace(" ", "").split(":")[1];*/
                            String number = phone.getText().toString();
                            if(number.contains("(0)")){
                                number = number.replace("(0)", "");
                            }
                            if(number.contains("/")){
                                number = number.replace("/", "");
                            }
                            if(number.contains("-")){
                                number = number.replace("-", "");
                            }
                            if(number.contains(" ")){
                                number = number.replace(" ", "");
                            }
                            if(number.contains(":")) {
                                intent.setData(Uri.parse("tel:" + number.split(":")[1].trim()));
                            }
                            else {
                                intent.setData(Uri.parse("tel:" + number.trim()));
                            }
                            startActivity(intent);

                        }
                    }
                }


            }
        });

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DirectoryActivity.this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DirectoryActivity.this, new String[]
                                        {Manifest.permission.CALL_PHONE}, 1);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(DirectoryActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(DirectoryActivity.this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {


                }
                return;
            }

        }
    }

    private void loadDirectoryData(String id){
        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                FrontEngine.getInstance().get_directory_data_url(id), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(!response.getString("title").equals("")) {
                                title.setText(response.getString("title"));
                            }
                            else{
                                title.setVisibility(View.GONE);
                            }

                            if(!response.getString("firstname").equals("")) {
                                name.setText(response.getString("firstname") + " " + response.getString("lastname"));
                            }
                            else{
                                name.setVisibility(View.GONE);
                            }

                            if(!response.getString("mail").equals("")) {
                                emailBtn.setVisibility(View.VISIBLE);
                                email.setText(response.getString("mail"));
                            }
                            else{
                                email.setVisibility(View.GONE);
                            }

                            if(!response.getString("street").equals("")) {
                                street.setText(response.getString("street"));
                            }
                            else{
                                street.setVisibility(View.GONE);
                            }

                            if(!response.getString("city").equals("")) {
                                city.setText(response.getString("city") + ", " + response.getString("postalCode"));
                            }
                            else{
                                city.setVisibility(View.GONE);
                            }

                            if(!response.getString("building").equals("")) {
                                building.setText(response.getString("building"));
                            }
                            else{
                                building.setVisibility(View.GONE);
                            }

                            if(!response.getString("office").equals("")) {
                                office.setText(response.getString("office"));
                            }
                            else{
                                office.setVisibility(View.GONE);
                            }

                            if(!response.getString("officeHour").equals("")) {
                                office_hours.setText(response.getString("officeHour"));
                            }
                            else{
                                office_hours.setVisibility(View.GONE);
                            }
                            if(!response.getString("phone").equals("")) {
                                call.setVisibility(View.VISIBLE);
                                phone.setText("Phone: " + response.getString("phone"));
                            }
                            else{
                                phone.setVisibility(View.GONE);
                            }
                            if(!response.getString("fax").equals("")) {
                                fax.setText("Fax: " + response.getString("fax"));
                            }
                            else{
                                fax.setVisibility(View.GONE);
                            }
                            if(!response.getString("webpage").equals("")) {
                                website.setText(response.getString("webpage"));
                            }
                            else{
                                website.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "NOO");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);
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
