package com.example.uniapp_20.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.uniapp_20.R;
import com.example.uniapp_20.SBActivities.EventActivity;
import com.example.uniapp_20.SBActivities.EventsSettingActivity;
import com.example.uniapp_20.adapter.EventAdapter;
import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.utils.FrontEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    ListView eventList;
    CalendarView calendarView;
    private EventAdapter eventAdapter;
    List<Event> updatedList;
    String currentDate, selectedDate;
    SimpleDateFormat sdf;
    Long date;
    public EventsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        calendarView = view.findViewById(R.id.events_calendar_view);
        eventList = view.findViewById(R.id.lv_events);
        date = calendarView.getDate();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date(calendarView.getDate()));
        updatedList = new ArrayList<>();
        final List<Event> events = FrontEngine.getInstance().getEventList();

        for(int i = 0; i < events.size(); i++){
            if(events.get(i).getHappeningDate().equals(currentDate)){
                updatedList.add(events.get(i));
            }
        }

        eventAdapter = new EventAdapter(view.getContext(), updatedList);
        eventList.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = view.getDate();
                updatedList.clear();
                String day = "";

                if(dayOfMonth < 9){
                    day = String.format("%02d", dayOfMonth);
                }
                else {
                    day = String.valueOf(dayOfMonth);
                }
                for(int i = 0; i < events.size(); i++){
                    if(events.get(i).getHappeningDate().equals(year + "-" + month+1 + "-" + day)){
                        updatedList.add(events.get(i));
                    }
                }
                eventAdapter.notifyDataSetChanged();
                //Toast.makeText(view.getContext(), "Year= " + year + " Month = " + month + " Date = " + dayOfMonth, Toast.LENGTH_LONG).show();

            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) eventList.getItemAtPosition(position);
                Intent newsIntent = new Intent(getContext(), EventActivity.class);
                newsIntent.putExtra("event", event);
                startActivity(newsIntent);
            }
        });

        return view;
    }

    private void selectedDate(){

    }
    // the create options menu with a MenuInflater to have the menu from your fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_events).setVisible(false);
        menu.findItem(R.id.action_back).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // Do onlick on menu action here
                startActivity(new Intent(getActivity(), EventsSettingActivity.class));
                getActivity().finish();
                return true;
            case R.id.action_back:
                // Do onlick on menu action here
                getActivity().onBackPressed();
                return true;
        }
        return false;
    }

}
