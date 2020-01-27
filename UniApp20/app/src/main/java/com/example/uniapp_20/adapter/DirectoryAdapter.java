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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DirectoryAdapter extends ArrayAdapter<Directory> implements Filterable {

    private Context mContext;
    private List<Directory> directoryList;


    public DirectoryAdapter(Context context, ArrayList<Directory> list){
        super(context, 0, list);
        this.mContext = context;
        this.directoryList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.directory_card, parent, false);

        }
        Directory directory = directoryList.get(position);
        if(directory.getPid() == null) {
            TextView name = view.findViewById(R.id.directory_fav_name);
            name.setText(directory.getNumber());
            TextView title = view.findViewById(R.id.directory_fav_title);
            title.setText(directory.getName());
        }
        else if(directory.getPid() != null){
            TextView name = view.findViewById(R.id.directory_fav_name);
            name.setText(directory.getName());
            TextView title = view.findViewById(R.id.directory_fav_title);
            title.setText(directory.getTitle());
        }
        return view;
    }

}
