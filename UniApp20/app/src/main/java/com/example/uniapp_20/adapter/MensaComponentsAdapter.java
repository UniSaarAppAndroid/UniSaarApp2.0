package com.example.uniapp_20.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.uniapp_20.R;

import java.util.logging.Filter;

public class MensaComponentsAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] mealComponent;
    private final String[] mealAllergens;

    public MensaComponentsAdapter(Activity context, String[] mealComponent, String[] mealAllergens) {
        super(context, R.layout.allergen_list_layout, mealComponent);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.mealComponent=mealComponent;
        this.mealAllergens=mealAllergens;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allergen_list_layout, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.mealComponent);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.allergen);

        titleText.setText(mealComponent[position]);
        //imageView.setImageResource(imgid[position]);
        subtitleText.setText(mealAllergens[position]);

        return rowView;

    };


}
