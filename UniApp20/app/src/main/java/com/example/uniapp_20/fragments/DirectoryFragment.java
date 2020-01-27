package com.example.uniapp_20.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.example.uniapp_20.SBActivities.DirectoryActivity;
import com.example.uniapp_20.adapter.DirectoryAdapter;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.model.Directory;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectoryFragment extends Fragment {

    ListView favList;
    private DirectoryAdapter directoryAdapter;
    EditText et_search;
    ArrayList<Directory> directoryList = new ArrayList<>();
    ArrayList<Directory> directoryListFiltered = new ArrayList<>();
    ArrayList<Directory> directoryShortList = new ArrayList<>();
    ArrayList<Directory> directoryShortListFiltered = new ArrayList<>();
    LinearLayout linearLayout;

    public DirectoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.getInstance(getContext()).setScreen("DirectoryFragment");

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_directory, container, false);
        getActivity().setTitle("Directory");

        favList = view.findViewById(R.id.lv_directory);
        et_search = view.findViewById(R.id.et_directory);
        linearLayout = view.findViewById(R.id.ll_helpfullnumbers);
        for (int i = 0; i<FrontEngine.getInstance().getHelpFullNumberList().size(); i++){
            String[] data = FrontEngine.getInstance().getHelpFullNumberList().get(i).split(":");
            directoryList.add(new Directory(data[0], data[1]));

        }

        if(directoryShortList.isEmpty()){
            directoryAdapter = new DirectoryAdapter(getContext(), directoryList);
            favList.setAdapter(directoryAdapter);
            directoryAdapter.notifyDataSetChanged();
        }
        else {
            directoryAdapter = new DirectoryAdapter(getContext(), directoryShortList);
            favList.setAdapter(directoryAdapter);
            directoryAdapter.notifyDataSetChanged();
        }
        favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Directory directory = (Directory) favList.getItemAtPosition(position);
                Intent directoryIntent = new Intent(getContext(), DirectoryActivity.class);
                if(directory.getPid() == null) {
                    directoryIntent.putExtra("helpfullnumbers", directory.getName() + ":" + directory.getNumber());
                    startActivity(directoryIntent);
                }
                else {
                    directoryIntent.putExtra("directory", directory.getPid());
                    startActivity(directoryIntent);
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String charString = s.toString();
                if(charString.length()>2) {
                    linearLayout.setVisibility(View.GONE);
                    directoryShortList.clear();
                    loadDirectory(charString);
                }
                else {
                    linearLayout.setVisibility(View.VISIBLE);
                    directoryShortList.clear();
                    directoryAdapter = new DirectoryAdapter(getContext(), directoryList);
                    favList.setAdapter(directoryAdapter);
                    directoryAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void loadDirectory(String query){
        // Tag used to cancel the request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                FrontEngine.getInstance().get_directory_url("0", "949", query), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Directory directory = new Directory(jsonObject.getString("name"), jsonObject.getString("title"), jsonObject.getString("pid"));
                                directoryShortList.add(directory);
                            }
                            if(directoryShortList.isEmpty()){
                                directoryAdapter = new DirectoryAdapter(getContext(), directoryList);
                                favList.setAdapter(directoryAdapter);
                                directoryAdapter.notifyDataSetChanged();
                            }
                            else {
                                directoryAdapter = new DirectoryAdapter(getContext(), directoryShortList);
                                favList.setAdapter(directoryAdapter);
                                directoryAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                SessionManager.getInstance(getContext()).setScreen("DirectoryFragment");
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(getContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("   Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Intent splashActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Intent splashActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Intent splashActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Intent splashActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), NoInternetConnectionActivity.class);
                    splashActivity.putExtra("Activity", "MainActivity");
                    startActivity(splashActivity);
                    getActivity().finish();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_favorite);
        if(item!=null)
            item.setVisible(false);
    }



}
