package com.example.uniapp_20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniapp_20.R;
import com.example.uniapp_20.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>  {
    private Context mContext;
    private List<News> newsList;
    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, name, headline, description;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            view.setTag(this);
            view.setOnClickListener(onItemClickListener);
            date = view.findViewById(R.id.card_date);
            name = view.findViewById(R.id.card_name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        return new NewsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.date.setText(news.getDate());
        holder.name.setText(news.getName());
        holder.description.setText(news.getDescription());
        if(news.getThumbnail().equals("")){
            holder.thumbnail.setVisibility(View.GONE);

        }
        else {
            // loading news cover using Glide library
            Glide.with(mContext).load(news.getThumbnail()).into(holder.thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
