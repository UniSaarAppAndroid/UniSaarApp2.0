package com.example.uniapp_20.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uniapp_20.R;
import com.example.uniapp_20.SBActivities.AboutActivity;
import com.example.uniapp_20.adapter.MoreAdapter;
import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    ListView lv_more;
    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SessionManager.getInstance(getContext()).setScreen("MoreFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        getActivity().setTitle("More");

        lv_more = view.findViewById(R.id.lv_more);
        MoreAdapter moreAdapter = new MoreAdapter(getContext(), FrontEngine.getInstance().getMoreList());
        lv_more.setAdapter(moreAdapter);
        moreAdapter.notifyDataSetChanged();

        lv_more.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = FrontEngine.getInstance().getMoreList().get(position);
                if(name.equals("Über uns")){
                    Intent aboutUs = new Intent(getActivity(), AboutActivity.class);
                    startActivity(aboutUs);
                }
                String[] link = name.split(":");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link[1]+":"+link[2]));
                startActivity(browserIntent);
            }
        });
        return view;
    }
    // the create options menu with a MenuInflater to have the menu from your fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_favorite).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

}