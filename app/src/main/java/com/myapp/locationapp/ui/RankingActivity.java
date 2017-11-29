package com.myapp.locationapp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myapp.locationapp.R;
import com.myapp.locationapp.adapter.RankingAdapter;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.Point;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TfTextView txtAlert;
    private RankingAdapter adapter;
    private List<Point> list;
    private ProgressBarHelper progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        init();
    }

    private void init() {

        txtAlert = (TfTextView) findViewById(R.id.txtAlert);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        initToolbar();

        recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20,20,20,20);
            }
        });
        list = new ArrayList<>();

        adapter = new RankingAdapter(this, list);
        recyclerView.setAdapter(adapter);

        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txtAlert.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtAlert.setVisibility(View.VISIBLE);
        }
        progressBar=new ProgressBarHelper(this,false);
        callApi();
    }

    private void callApi() {
        progressBar.showProgressDialog();
        AppApi api= MyApplication.getRetrofit().create(AppApi.class);
        api.getPoint().enqueue(new Callback<BaseResponse<Point>>() {
            @Override
            public void onResponse(Call<BaseResponse<Point>> call, Response<BaseResponse<Point>> response) {
                progressBar.hideProgressDialog();
                if(response.body()!=null&&response.body().getStatus()==1&&response.body().getData()!=null&&response.body().getData().size()>0){
                    list=response.body().getData();
                    adapter.setDataList(list);
                    recyclerView.setVisibility(View.VISIBLE);
                    txtAlert.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    txtAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Point>> call, Throwable t) {
                progressBar.hideProgressDialog();
                recyclerView.setVisibility(View.GONE);
                txtAlert.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(RankingActivity.this, false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callApi();
    }
}
