package com.example.uniapp_20.SBActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.R;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.model.Mensa;
import com.example.uniapp_20.utils.FrontEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.android.volley.VolleyLog.TAG;

public class MensaIngredientActivity extends AppCompatActivity implements Serializable {

    private Toolbar toolbar;
    CardView cardView2, cardView3;
    TextView mensa_card_color,mensa_card_color2,mensa_card_color3, mensa_card_time, mensa_card_headline, mensa_card_name, mensa_card_description, mensa_card_description2;
    TextView mensa_card_general_notices_text, mensa_card_meal_component_text;
    private TextView mensa_meal;
    Mensa mensa;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_ingredient);

        mensa = (Mensa) getIntent().getSerializableExtra("mensa");
        toolbar = findViewById(R.id.mensa_ingredient_toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        cardView2 = findViewById(R.id.card_view2);
        cardView3 = findViewById(R.id.card_view3);
        mensa_card_color = findViewById(R.id.mensa_card_color);
        mensa_card_color2 = findViewById(R.id.mensa_card_color2);
        mensa_card_color3 = findViewById(R.id.mensa_card_color3);
        mensa_card_time = findViewById(R.id.mensa_card_time);
        mensa_card_headline = findViewById(R.id.mensa_card_headline);
        mensa_card_description = findViewById(R.id.mensa_card_description);
        mensa_card_description2 = findViewById(R.id.mensa_card_description2);
        mensa_card_name = findViewById(R.id.mensa_card_name);
        mensa_card_meal_component_text = findViewById(R.id.mensa_card_meal_component_text);
        mensa_card_general_notices_text = findViewById(R.id.mensa_card_general_notices_text);


        mensa_card_color.setBackgroundColor(FrontEngine.getHexColor(mensa.getColor()));
        mensa_card_color2.setBackgroundColor(FrontEngine.getHexColor(mensa.getColor()));
        mensa_card_color3.setBackgroundColor(FrontEngine.getHexColor(mensa.getColor()));
        mensa_card_time.setText(mensa.getTime());
        mensa_card_headline.setText(mensa.getHeadline());
        mensa_card_description2.setText(mensa.getPrices());
        mensa_card_name.setText(mensa.getName());
        loadMensaDetail();
    }

    /**
     * Adding few mensa items for testing
     */
    private void loadMensaDetail() {
        String mensaUrl = FrontEngine.getInstance().base_url + "mensa/mealDetail?meal="+ mensa.getId() + "&language=" + FrontEngine.getInstance().language;

        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                mensaUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            String name = response.getString("mealName");
                            String desc = response.getString("description");
                            mensa_card_description.setText(desc);


                            JSONObject color = response.getJSONObject("color");
                            int r = color.getInt("r");
                            int g = color.getInt("g");
                            int b = color.getInt("b");
                            int[] rgb = {r, g, b};


                            StringBuilder generalNoticeText = new StringBuilder();
                            JSONArray jsonArray = response.getJSONArray("generalNotices");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject generalNotices = jsonArray.getJSONObject(i);
                                String notice = generalNotices.getString("notice");
                                String dislayName = generalNotices.getString("displayName");
                                generalNoticeText.append(dislayName).append("\n");
                                //generalNoticeText.append(notice).append(": ").append(dislayName).append("\n");
                            }
                            if(generalNoticeText.length() > 0) {
                                mensa_card_general_notices_text.setText(generalNoticeText);
                            }
                            else {
                                cardView2.setVisibility(View.GONE);
                            }
                            JSONArray jsonArray1 = response.getJSONArray("mealComponents");
                            StringBuilder noticeStr = new StringBuilder();

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject mealComponent = jsonArray1.getJSONObject(i);
                                String componentName = mealComponent.getString("componentName");
                                JSONArray notices = mealComponent.getJSONArray("notices");
                                noticeStr.append(componentName).append("\n");
                                for(int j = 0; j<notices.length(); j++){
                                    JSONObject jsonObject = notices.getJSONObject(j);
                                    String notice = jsonObject.getString("notice");
                                    String displayName = jsonObject.getString("displayName");
                                   noticeStr.append("\t - " + displayName).append("\n");
                                   //noticeStr.append("\t - "+notice).append(": ").append(displayName).append("\n");
                                }
                            }
                            if(noticeStr.length() > 0) {
                                mensa_card_meal_component_text.setText(noticeStr + "\n");
                            }
                            else{
                                cardView3.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

}
