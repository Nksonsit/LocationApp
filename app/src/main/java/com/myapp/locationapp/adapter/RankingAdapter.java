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
import com.myapp.locationapp.model.Ranking;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by ishan on 22-11-2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ranking> list;
    private Context context;

    public RankingAdapter(Context context, List<Ranking> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TfTextView txtName,txtRank;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TfTextView) itemView.findViewById(R.id.txtName);
            txtRank= (TfTextView) itemView.findViewById(R.id.txtRank);
            img = (ImageView) itemView.findViewById(R.id.img);
        }

        public void setValues(Ranking ranking) {
            Glide.with(context).load(ranking.getUserImage()).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(img);
            txtName.setText(ranking.getName());
            txtRank.setText(ranking.getRank()+"");

        }
    }
}
