package com.example.uniapp_20.SBActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uniapp_20.R;
import com.example.uniapp_20.adapter.EventAdapter;
import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.utils.FrontEngine;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.graphics.Color.RED;
import static android.graphics.Color.green;

public class EventListActivity extends AppCompatActivity {


    private AppBarLayout appBarLayout;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

    private CompactCalendarView compactCalendarView;

    private SimpleDateFormat eventSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    ListView eventList;
    private EventAdapter eventAdapter;
    List<Event> updatedList;
    String currentDate, selectedDate;
    SimpleDateFormat sdf;
    Long date;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Events");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        appBarLayout = findViewById(R.id.app_bar_layout);



        // Set up the CompactCalendarView
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);


        eventList = findViewById(R.id.lv_events);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date());
        updatedList = new ArrayList<>();
        final List<Event> events = FrontEngine.getInstance().getEventList();
        for(int i = events.size(); i > 0; i--){
            if(events.get(i-1).getHappeningDate().equals(currentDate)){
                updatedList.add(events.get(i));
            }
            else {
                Date date = null;
                try {
                    date = eventSimpleDateFormat.parse(events.get(i-1).getHappeningDate());
                    long output=date.getTime()/1000L;
                    String str=Long.toString(output);
                    long timestamp = Long.parseLong(str) * 1000;
                    com.github.sundeepk.compactcalendarview.domain.Event upcomingEvent = new com.github.sundeepk.compactcalendarview.domain.Event(RED, timestamp, events.get(i-1).getHappeningDate());
                    compactCalendarView.addEvent(upcomingEvent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        eventAdapter = new EventAdapter(getApplicationContext(), updatedList);
        eventList.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();


        // Set current date to today
        setCurrentDate(new Date());

        final ImageView arrow = findViewById(R.id.date_picker_arrow);

        final float[] rotation = {isExpanded ? 0 : 180};
        ViewCompat.animate(arrow).rotation(rotation[0]).start();

        isExpanded = !isExpanded;
        appBarLayout.setExpanded(isExpanded, true);

        RelativeLayout datePickerButton = findViewById(R.id.date_picker_button);

        datePickerButton.setOnClickListener(v -> {
            rotation[0] = isExpanded ? 0 : 180;
            ViewCompat.animate(arrow).rotation(rotation[0]).start();

            isExpanded = !isExpanded;
            appBarLayout.setExpanded(isExpanded, true);
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                updatedList.clear();
                String day = "";
                int dayOfMonth = dateClicked.getDate();
                String year = String.valueOf(sdf.format(dateClicked).split("-")[0]);
                if(dayOfMonth < 9){
                    day = String.format("%02d", dayOfMonth);
                }
                else {
                    day = String.valueOf(dayOfMonth);
                }
                for(int i = 0; i < events.size(); i++){
                    if(events.get(i).getHappeningDate().equals(year + "-" + dateClicked.getMonth()+1 + "-" + day)){
                        updatedList.add(events.get(i));
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) eventList.getItemAtPosition(position);
                Intent newsIntent = new Intent(getBaseContext(), EventActivity.class);
                newsIntent.putExtra("event", String.valueOf(event.getId()));
                startActivity(newsIntent);
            }
        });

    }
    private void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.title);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_events).setVisible(false);
        menu.findItem(R.id.action_favorite).setVisible(false);
        menu.findItem(R.id.action_back).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            finish();
            return true;
        }
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
