package com.example.uniapp_20.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.NoInternetConnectionActivity;
import com.example.uniapp_20.R;
import com.example.uniapp_20.SBActivities.MensaIngredientActivity;
import com.example.uniapp_20.adapter.MensaAdapter;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.model.Mensa;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class FirstMenuFragment extends Fragment{

    private MensaAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mensaFirstDate;

    public FirstMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_menu, container, false);
        //getActivity().setTitle("Mensa Menu " + "("+FrontEngine.getInstance().mensaFirstDate+")");

        getActivity().setTitle("Mensa Menu");
        mensaFirstDate = view.findViewById(R.id.mensa_first_date);
        mensaFirstDate.setText(FrontEngine.getInstance().mensaFirstDate);

        mSwipeRefreshLayout = view.findViewById(R.id.mensa_swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMensa();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.mensa_first_recycler_view);

        adapter = new MensaAdapter(view.getContext(), FrontEngine.getInstance().getmensaListFirst());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Mensa mensa = FrontEngine.getInstance().getmensaListFirst().get(position);
                Intent ingredientIntent = new Intent(getContext(), MensaIngredientActivity.class);
                ingredientIntent.putExtra("mensa", mensa);
                startActivity(ingredientIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        adapter.notifyDataSetChanged();
        return view;
    }

    /**
     * Adding few mensa items for testing
     */
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
                            FrontEngine.getInstance().getmensaListFirst().clear();
                            JSONArray jsonArray = response.getJSONArray("days");
                            for(int a = 0; a < jsonArray.length(); a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                String menuDate = jsonObject.getString("date");
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
                                            componentsName.append(", ");
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
                            mSwipeRefreshLayout.setRefreshing(false);
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
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "SplashActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);

    }



    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
