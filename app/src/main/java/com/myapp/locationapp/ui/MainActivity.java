package com.myapp.locationapp.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.myapp.locationapp.R;
import com.myapp.locationapp.custom.TfButton;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;

public class MainActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private RelativeLayout btnNewsHeadline;
    private RelativeLayout btnLink;
    private RelativeLayout btnRanking;
    private ImageView imgLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        actionListener();
    }

    private void actionListener() {
        btnNewsHeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this,view);
                Functions.fireIntent(MainActivity.this,NewsActivity.class,true);
            }
        });
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this,view);
                Functions.fireIntent(MainActivity.this,AddDlUlActivity.class,true);

            }
        });
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this,view);
                Functions.fireIntent(MainActivity.this,RankingActivity.class,true);

            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.logout(MainActivity.this);
            }
        });
    }

    private void init() {
        btnRanking = (RelativeLayout) findViewById(R.id.btnRanking);
        btnLink = (RelativeLayout) findViewById(R.id.btnLink);
        btnNewsHeadline = (RelativeLayout) findViewById(R.id.btnNewsHeadline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgLogout= (ImageView) findViewById(R.id.imgLogout);
        txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(),"fonts/speed2.ttf"));

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setTitle("");
    }
}
