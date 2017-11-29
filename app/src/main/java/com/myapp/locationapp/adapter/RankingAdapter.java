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
import com.myapp.locationapp.model.Point;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by ishan on 22-11-2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Point> list;
    private Context context;

    public RankingAdapter(Context context, List<Point> list) {
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

    public void setDataList(List<Point> list) {
        this.list=new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtName,txtRank;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TfTextView) itemView.findViewById(R.id.txtName);
            txtRank= (TfTextView) itemView.findViewById(R.id.txtRank);
        }

        public void setValues(Point point) {
            txtName.setText(point.getFirstName()+" "+point.getLastName());
            txtRank.setText(point.getPoint());

        }
    }
}
