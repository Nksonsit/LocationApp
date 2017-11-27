package com.myapp.locationapp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.myapp.locationapp.R;
import com.myapp.locationapp.adapter.NewsAdapter;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.AddNewsDialog;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private TfTextView txtAddNews;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TfTextView txtAlert;
    private List<News> list;
    private NewsAdapter adapter;
    private ProgressBarHelper progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
        actionListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callApi();
    }

    private void actionListener() {
        txtAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(NewsActivity.this, view);
                new AddNewsDialog(NewsActivity.this, new AddNewsDialog.OnAddNews() {
                    @Override
                    public void onAddNews(String txtNews) {
                        News news = new News();
                        news.setNews(txtNews);
                        news.setUserId(PrefUtils.getUserID(NewsActivity.this));
                        list.add(news);
                        adapter.setDataList(list);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtAlert.setVisibility(View.GONE);

                        addNews(news);
                    }
                });
            }
        });
    }

    private void addNews(News news) {
        progressBar.showProgressDialog();
        Log.e("news add req", MyApplication.getGson().toJson(news));
        AppApi appApi=MyApplication.getRetrofit().create(AppApi.class);
        appApi.addNewsHeadlines(news).enqueue(new Callback<BaseResponse<News>>() {
            @Override
            public void onResponse(Call<BaseResponse<News>> call, Response<BaseResponse<News>> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null && response.body().getStatus() == 1) {
                    Log.e("News add res", MyApplication.getGson().toJson(response.body()));
                } else {
                    Functions.showToast(NewsActivity.this, getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<News>> call, Throwable t) {
                progressBar.hideProgressDialog();
                Functions.showToast(NewsActivity.this, getString(R.string.try_again));
            }
        });
    }

    private void init() {
        progressBar = new ProgressBarHelper(this, false);
        txtAlert = (TfTextView) findViewById(R.id.txtAlert);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtAddNews = (TfTextView) findViewById(R.id.txtAddNews);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        initToolbar();

        recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(15, 15, 15, 15);
            }
        });
        list = new ArrayList<>();

        adapter = new NewsAdapter(this, list);
        recyclerView.setAdapter(adapter);

        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txtAlert.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtAlert.setVisibility(View.VISIBLE);
        }

        callApi();
    }

    private void callApi() {
        progressBar.showProgressDialog();
        AppApi api = MyApplication.getRetrofit().create(AppApi.class);
        api.getNewsHeadlines().enqueue(new Callback<BaseResponse<News>>() {
            @Override
            public void onResponse(Call<BaseResponse<News>> call, Response<BaseResponse<News>> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null && response.body().getStatus() == 1) {
                    Log.e("News res", MyApplication.getGson().toJson(response.body()));
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        list = response.body().getData();
                        adapter.setDataList(list);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtAlert.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        txtAlert.setVisibility(View.VISIBLE);
                    }
                } else {
                    Functions.showToast(NewsActivity.this, getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<News>> call, Throwable t) {
                progressBar.showProgressDialog();
                Functions.showToast(NewsActivity.this, getString(R.string.try_again));
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
        Functions.fireIntent(NewsActivity.this, false);
    }
}
