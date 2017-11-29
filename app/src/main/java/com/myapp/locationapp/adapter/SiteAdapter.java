package com.myapp.locationapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.myapp.locationapp.R;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.model.Site;
import com.myapp.locationapp.ui.AddDlUlActivity;
import com.myapp.locationapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 22-11-2017.
 */

public class SiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Site> list;
    private Context context;

    public SiteAdapter(Context context, List<Site> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_site, parent, false);
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

    public void setDataList(List<Site> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtSite, txtDesc, txtDistance,txtAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSite = (TfTextView) itemView.findViewById(R.id.txtSite);
            txtDesc = (TfTextView) itemView.findViewById(R.id.txtDesc);
            txtDistance = (TfTextView) itemView.findViewById(R.id.txtDistance);
            txtAdd= (TfTextView) itemView.findViewById(R.id.txtAdd);
        }

        public void setValues(Site site) {
            txtDistance.setText(site.getDistance());
            txtDesc.setText(site.getDescription());
            txtSite.setText(site.getSite());

            txtAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Functions.hideKeyPad(context, view);
                    Intent intent=new Intent(context, AddDlUlActivity.class);
                    intent.putExtra("site",list.get(getAdapterPosition()));
                    Functions.fireIntent(context, intent, true);
                }
            });

        }
    }
}
