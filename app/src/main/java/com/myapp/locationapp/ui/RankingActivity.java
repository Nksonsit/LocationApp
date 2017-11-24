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
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.model.Ranking;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TfTextView txtAlert;
    private RankingAdapter adapter;
    private List<Ranking> list;

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
                outRect.set(15, 15, 15, 15);
            }
        });
        list = new ArrayList<>();


        Ranking ranking1 = new Ranking();
        ranking1.setName("Phoebe");
        ranking1.setRank(4);
        ranking1.setUserImage("https://lgbtfansdeservebetter.com/static/uploads/2016/09/Phoebe-Buffay-Lisa-Kudrow.png");

        Ranking ranking2 = new Ranking();
        ranking2.setName("Rachel");
        ranking2.setRank(3);
        ranking2.setUserImage("https://i.pinimg.com/originals/b3/7d/bf/b37dbfbbbaa71221d21bca303b20fe51.jpg");

        Ranking ranking3 = new Ranking();
        ranking3.setName("Ross");
        ranking3.setRank(5);
        ranking3.setUserImage("https://upload.wikimedia.org/wikipedia/en/6/6f/David_Schwimmer_as_Ross_Geller.jpg");

        Ranking ranking4 = new Ranking();
        ranking4.setName("Monica");
        ranking4.setRank(2);
        ranking4.setUserImage("https://vignette.wikia.nocookie.net/friends/images/7/75/Monica.jpg/revision/latest?cb=20130802071219");

        Ranking ranking5 = new Ranking();
        ranking5.setName("Chandler");
        ranking5.setRank(1);
        ranking5.setUserImage("https://vignette.wikia.nocookie.net/friends/images/c/cc/Square_Chandler.jpg/revision/latest?cb=20111216200026");

        Ranking ranking6 = new Ranking();
        ranking6.setName("Joey");
        ranking6.setRank(6);
        ranking6.setUserImage("https://upload.wikimedia.org/wikipedia/en/d/da/Matt_LeBlanc_as_Joey_Tribbiani.jpg");

        list.add(ranking1);
        list.add(ranking2);
        list.add(ranking3);
        list.add(ranking4);
        list.add(ranking5);
        list.add(ranking6);
        adapter = new RankingAdapter(this, list);
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
        Functions.fireIntent(RankingActivity.this, false);
    }
}
