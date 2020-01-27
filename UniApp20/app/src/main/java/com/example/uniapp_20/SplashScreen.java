package com.example.uniapp_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.SBActivities.MainActivity;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.model.Mensa;
import com.example.uniapp_20.model.News;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.NotifyWorker;
import com.example.uniapp_20.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static com.android.volley.VolleyLog.TAG;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SessionManager.getInstance(getBaseContext()).setSwitchDE(true);
        SessionManager.getInstance(getBaseContext()).setSwitchFR(false);
        SessionManager.getInstance(getBaseContext()).setSwitchEN(false);
        SessionManager.getInstance(getBaseContext()).setSwitchOne(true);
        SessionManager.getInstance(getBaseContext()).setSwitchTwo(true);
        SessionManager.getInstance(getBaseContext()).setSwitchThree(true);
        SessionManager.getInstance(getBaseContext()).setSwitchFour(true);
        SessionManager.getInstance(getBaseContext()).setSwitchFive(true);
        SessionManager.getInstance(getBaseContext()).setSwitchSix(true);
        helpfulNumbers();
        loadMore();
        loadMensa();


        if (SessionManager.getInstance(getBaseContext()).switchFR()) {
            FrontEngine.getInstance().language = "fr";
        }
        if (SessionManager.getInstance(getBaseContext()).switchEN()) {
            FrontEngine.getInstance().language = "en";
        } else {
            FrontEngine.getInstance().language = "de";
        }

    }

    private Data createWorkInputData(String title, String text, int id){
        return new Data.Builder()
                .putString(FrontEngine.EXTRA_TITLE, title)
                .putString(FrontEngine.EXTRA_TEXT, text)
                .putInt(FrontEngine.EXTRA_ID, id)
                .build();
    }

    private long getAlertTime(int userInput){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, userInput);
        return cal.getTimeInMillis();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private void loadMensa() {
        String mensaUrl = FrontEngine.getInstance().base_url + "mensa/mainScreen?location=";

        if (FrontEngine.getInstance().isHomburg) {
            mensaUrl += "hom";
        } else {
            mensaUrl += "sb";
        }

        mensaUrl += "&language=";

        mensaUrl += FrontEngine.getInstance().language;
        Log.d(TAG, mensaUrl);


        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                mensaUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("days");

                            for(int a = 0; a < 2; a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                String menuDate = jsonObject.getString("date") + Calendar.getInstance().get(Calendar.YEAR);
                                if(a == 0){

                                    FrontEngine.getInstance().mensaFirstDate = menuDate;

                                }
                                else {
                                    FrontEngine.getInstance().mensaSecondDate = menuDate;

                                }

                                JSONArray counters = jsonObject.getJSONArray("meals");
                                for (int i = 0; i < counters.length(); i++) {

                                    JSONObject menu = counters.getJSONObject(i);

                                    int id = menu.getInt("id");

                                    String displayName = menu.getString("counterName");


                                    JSONObject color = menu.getJSONObject("color");
                                    int r = color.getInt("r");
                                    int g = color.getInt("g");
                                    int b = color.getInt("b");
                                    int[] rgb = {r, g, b};

                                    String openTime = menu.getString("openingHours");
                                    if (FrontEngine.getInstance().language == "de") {
                                        openTime += " Uhr";
                                    }

                                    String name = menu.getString("mealName");

                                    JSONArray components = menu.getJSONArray("components");
                                    StringBuilder componentsName = new StringBuilder();

                                    String prices = "";

                                    if (menu.has("prices")) {
                                        JSONArray price = menu.getJSONArray("prices");

                                        for (int j = 0; j < price.length(); j++) {
                                            JSONObject o = price.getJSONObject(j);
                                            prices += o.get("priceTag");
                                            prices += ": ";
                                            String s = o.get("price").toString();
                                            String[] pr = s.split("[.]");


                                            if (pr.length > 0) {
                                                if (pr[1].length() == 1) {
                                                    prices += pr[0] + "," + pr[1] + "0";
                                                }
                                                if (pr[1].length() == 2) {
                                                    prices += pr[0] + "," + pr[1];
                                                }
                                                if (pr[1].length() == 0) {
                                                    prices += pr[0] + "," + pr[1] + "00";
                                                }
                                            } else {
                                                prices += s;
                                            }
                                            prices += "€ ";

                                        }
                                    }

                                    for (int j = 0; j < components.length(); j++) {
                                        String data = components.getString(j);
                                        componentsName.append(data);
                                        if (j < components.length() - 1) {
                                            componentsName.append("\n");
                                        }
                                    }

                                    JSONArray notices = menu.getJSONArray("notices");
                                    StringBuilder noticesName = new StringBuilder();
                                    for (int j = 0; j < notices.length(); j++) {
                                        String data = notices.getString(j);
                                        noticesName.append(data);
                                        if (j < notices.length() - 1) {
                                            noticesName.append(", ");
                                        }
                                    }

                                    Mensa mensa = new Mensa(id, a, menuDate, openTime, name, componentsName.toString(), displayName, rgb, prices, notices.toString());
                                    System.out.println(mensa);
                                    if(a == 0){
                                        FrontEngine.getInstance().addMenuFirst(mensa);
                                    }
                                    else {
                                        FrontEngine.getInstance().addMenuSecond(mensa);
                                    }
                                }

                            }
                            String text = FrontEngine.getInstance().getmensaListFirst().get(0).getHeadline()+ ", "+
                                    FrontEngine.getInstance().getmensaListFirst().get(1).getHeadline()+ ", "+
                                    FrontEngine.getInstance().getmensaListFirst().get(2).getHeadline()+  ", " +
                                    " and much more";
                            //Generate notification string tag
                            String tag = "notify";

                            int random = (int )(Math.random() * 50 + 1);
                            NotifyWorker.cancelReminder(tag);
                            //Data
                            Data data = createWorkInputData(FrontEngine.TITLE, text, random);

                            NotifyWorker.scheduleReminder(data, tag);
                            loadNews();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);

    }

    private void loadNews() {
        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,

                FrontEngine.getInstance().get_news_url("0", "949", FrontEngine.getInstance().language), null,
                response -> {

                    try {
                        FrontEngine.getInstance().getNewsList().clear();
                        int itemCount = response.getInt("itemCount");
                        boolean hasNextPage = response.getBoolean("hasNextPage");

                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject item = jsonArray.getJSONObject(i);
                            boolean isEvent = item.getBoolean("isEvent");
                            String publishedDate = item.getString("publishedDate");
                            int id = item.getInt("id");
                            String happeningDate = "";
                            if (item.has("happeningDate")) {
                                happeningDate = item.getString("happeningDate");
                            }
                            String title = item.getString("title");
                            JSONObject categories = item.getJSONObject("categories");
                            ArrayList<String> category = new ArrayList<>();
                            for (int j = 0; j < categories.length(); j++) {
                                String key = categories.keys().next();
                                category.add(categories.getString(key));
                                if (isEvent) {
                                    Log.d("EVENT", "EVE_CAT");
                                }
                                if (!FrontEngine.getInstance().getCategoryList().contains(categories.getString(key))) {
                                    FrontEngine.getInstance().addCategory(categories.getString(key));
                                }
                            }

                            String description = item.getString("description");
                            String imageUrl = item.getString("imageURL");
                            if (!isEvent) {
                                News news = new News(id, publishedDate, title, description, imageUrl, category);
                                FrontEngine.getInstance().addNews(news);
                            } else {
                                Event event = new Event(id, happeningDate, title, description);
                                FrontEngine.getInstance().addEvent(event);
                            }

                        }
                        loadDefaults();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);
    }

    public void loadDefaults() {


        if (SessionManager.getInstance(getBaseContext()).isSB()) {
            FrontEngine.getInstance().isSB = true;
            Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        } else if (SessionManager.getInstance(getApplicationContext()).isHB()) {
            FrontEngine.getInstance().isSB = false;
            Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        } else {
            Intent mainActivity = new Intent(SplashScreen.this, SetupActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }



    /**
     * Load More links from server
     */
    private void loadMore() {
        String moreUrl = FrontEngine.getInstance().base_url + "more?language="+FrontEngine.getInstance().language+"&lastUpdated=never";

        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                moreUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            FrontEngine.getInstance().getMoreList().clear();

                            JSONArray jsonArray = response.getJSONArray("links");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String link = jsonObject.getString("link");
                                FrontEngine.getInstance().addMore(name+":"+link);

                            }
                            FrontEngine.getInstance().addMore("Über uns");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);

    }

    private void helpfulNumbers(){
        String moreUrl = FrontEngine.getInstance().base_url + "directory/helpfulNumbers?language="+FrontEngine.getInstance().language+"&lastUpdated=never";

        // Tag used to cancel the request
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                moreUrl, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            FrontEngine.getInstance().getHelpFullNumberList().clear();
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String number = jsonObject.getString("number");
                                String link = "";
                                String mail = "";
                                if(jsonObject.has("link")) {
                                    link = jsonObject.getString("link");
                                }
                                if(jsonObject.has("mail")) {
                                    mail = jsonObject.getString("mail");
                                }
                                FrontEngine.getInstance().addHelpFullNumberList(name+":"+number + ":"+link +":" +mail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(SplashScreen.this, NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    finish();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);

    }

}
