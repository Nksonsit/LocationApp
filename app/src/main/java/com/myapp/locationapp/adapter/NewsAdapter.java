package com.myapp.locationapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myapp.locationapp.R;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 22-11-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<News> list;
    private Context context;

    public NewsAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setValues(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataList(List<News> list) {
        this.list=new ArrayList<>();
        this.list=list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtNews;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNews = (TfTextView) itemView.findViewById(R.id.txtNews);
        }

        public void setValues(News news) {
            txtNews.setText(news.getNews());
        }
    }
}
