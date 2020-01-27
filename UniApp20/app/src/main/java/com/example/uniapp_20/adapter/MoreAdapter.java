package com.example.uniapp_20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uniapp_20.R;
import com.example.uniapp_20.model.Directory;

import java.util.ArrayList;
import java.util.List;

public class MoreAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> moreList;


    public MoreAdapter(Context context, List<String> list) {
        super(context, 0, list);
        this.mContext = context;
        this.moreList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.more_card, parent, false);

        }
        String list = moreList.get(position);
        TextView name = view.findViewById(R.id.more_name);
        name.setText(list.split(":")[0]);

        return view;
    }
}