package com.example.uniapp_20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniapp_20.R;
import com.example.uniapp_20.model.Mensa;
import com.example.uniapp_20.utils.FrontEngine;

import java.util.List;

public class MensaAdapter extends RecyclerView.Adapter<MensaAdapter.MyViewHolder> {

    private Context mContext;
    private List<Mensa> mensaList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mensa_card_color, mensa_card_time, mensa_card_headline, mensa_card_name, mensa_card_description, mensa_card_date;

        public MyViewHolder(View view) {

            super(view);
            mensa_card_color = view.findViewById(R.id.mensa_card_color);
            mensa_card_time = view.findViewById(R.id.mensa_card_time);
            mensa_card_headline = view.findViewById(R.id.mensa_card_headline);
            mensa_card_description = view.findViewById(R.id.mensa_card_description);
            mensa_card_name = view.findViewById(R.id.mensa_card_name);

        }
    }

    public MensaAdapter(Context mContext, List<Mensa> mensaList){
        this.mContext = mContext;
        this.mensaList = mensaList;
    }

    @Override
    public MensaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mensa_card, parent, false);

        return new MensaAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MensaAdapter.MyViewHolder holder, int position) {
        Mensa mensa = mensaList.get(position);
        holder.mensa_card_color.setBackgroundColor(FrontEngine.getHexColor(mensa.getColor()));
        holder.mensa_card_time.setText(mensa.getTime());
        holder.mensa_card_headline.setText(mensa.getHeadline());
        holder.mensa_card_description.setText(mensa.getDescription());
        holder.mensa_card_name.setText(mensa.getName());

    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_mensa, popup.getMenu());
        popup.setOnMenuItemClickListener(new MensaAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mensaList.size();
    }
}
