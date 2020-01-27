package com.example.uniapp_20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uniapp_20.R;
import com.example.uniapp_20.model.Directory;
import com.example.uniapp_20.model.Event;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> implements Filterable {

    private Context mContext;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> list){
        super(context, 0, list);
        this.mContext = context;
        this.eventList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.event_card, parent, false);

        }
        Event event = eventList.get(position);

        TextView headline = view.findViewById(R.id.event_headline);
        TextView location = view.findViewById(R.id.event_location);
        headline.setText(event.getHeadline());
        location.setText(event.getLocation());
        return view;
    }

}
