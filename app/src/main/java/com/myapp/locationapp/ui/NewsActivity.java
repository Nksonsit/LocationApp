package com.myapp.locationapp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myapp.locationapp.R;
import com.myapp.locationapp.adapter.NewsAdapter;
import com.myapp.locationapp.custom.AddNewsDialog;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private TfTextView txtAddNews;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TfTextView txtAlert;
    private List<News> list;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
        actionListener();
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
                        news.setNewsImgUrl("https://www.wefornews.com/wp-content/uploads/2017/01/news-3.jpg");
                        list.add(news);
                        adapter.setDataList(list);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtAlert.setVisibility(View.GONE);
                        Functions.showToast(NewsActivity.this, "Successfully Added");
                    }
                });
            }
        });
    }

    private void init() {
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

        News news = new News();
        for (int i = 0; i < 10; i++) {
            news.setNews("news " +(i+1));
            news.setNewsImgUrl("https://www.wefornews.com/wp-content/uploads/2017/01/news-3.jpg");
            list.add(news);
        }
        adapter = new NewsAdapter(this, list);
        recyclerView.setAdapter(adapter);

        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txtAlert.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtAlert.setVisibility(View.VISIBLE);
        }
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
