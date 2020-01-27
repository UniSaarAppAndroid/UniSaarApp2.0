package com.example.uniapp_20.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.SBActivities.EventListActivity;
import com.example.uniapp_20.SBActivities.NewsActivity;
import com.example.uniapp_20.SBActivities.NewsFeedSettingActivity;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.model.News;
import com.example.uniapp_20.adapter.NewsAdapter;
import com.example.uniapp_20.R;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private FragmentActivity myContext;
    private NewsAdapter adapter;
    List<News> newsList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            News news = FrontEngine.getInstance().getNewsList().get(position);
            Intent newsIntent = new Intent(getContext(), NewsActivity.class);
            newsIntent.putExtra("news", String.valueOf(news.getId()));
            startActivity(newsIntent);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.getInstance(getContext()).setScreen("NewsFragment");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        if(!SessionManager.getInstance(getContext()).switchDE()){
            loadNews();
        }
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh_items);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        for(int i = 0; i < FrontEngine.getInstance().getEventList().size(); i++){
            News news = FrontEngine.getInstance().getNewsList().get(i);
            if(SessionManager.getInstance(getContext()).switchOne()){
                if(news.getArrayList().contains("Veranstaltungen")){
                    newsList.add(news);
                    continue;
                }
            }
            if(SessionManager.getInstance(getContext()).switchTwo()){
                if(news.getArrayList().contains("Veranstaltungen für Studieninteressierte")){
                    newsList.add(news);
                    continue;
                }
            }
            if(SessionManager.getInstance(getContext()).switchThree()){
                if(news.getArrayList().contains("News / Pressemitteilungen")){
                    newsList.add(news);
                    continue;
                }
            }
            if(SessionManager.getInstance(getContext()).switchFour()){
                if(news.getArrayList().contains("Forschung (keine Veranstaltungen)")){
                    newsList.add(news);
                    continue;
                }
            }
            if(SessionManager.getInstance(getContext()).switchFive()){
                if(news.getArrayList().contains("Startseite")){
                    newsList.add(news);
                    continue;
                }
            }
            if(SessionManager.getInstance(getContext()).switchSix()){
                if(news.getArrayList().contains("Schüler")){
                    newsList.add(news);
                    continue;
                }
            }
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                loadNews();
            }
        });
        adapter = new NewsAdapter(view.getContext(), newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(onItemClickListener);
        adapter.notifyDataSetChanged();
        return view;
    }

    private void loadNews() {
        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                FrontEngine.getInstance().get_news_url("0", "949", FrontEngine.getInstance().language), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            FrontEngine.getInstance().getEventList().clear();
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
                            mSwipeRefreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();

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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
    // the create options menu with a MenuInflater to have the menu from your fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_events).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // Do onlick on menu action here
                startActivity(new Intent(getActivity(), NewsFeedSettingActivity.class));
                getActivity().finish();
                return true;
            case R.id.action_events:
                // Do onlick on menu action here
                startActivity(new Intent(getActivity(), EventListActivity.class));
                return true;
        }
        return false;
    }

}
