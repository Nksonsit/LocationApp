package com.myapp.locationapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.myapp.locationapp.R;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.TfButton;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.GPSTracker;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.Point;
import com.myapp.locationapp.model.Site;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private TfTextView txtSite;
    private TfTextView txtDesc;
    private TfButton btnAccept;
    private TfButton btnIgnore;
    private LinearLayout bottomView;
    private Site site;
    private ProgressBarHelper progressBar;
    private double latitude = 0;
    private double longitude = 0;
    private boolean isAccept = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        init();
        actionListener();

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("lat long", gpsTracker.getLatitude() + " " + gpsTracker.getLongitude());
        }
    }

    @Override
    public void onBackPressed() {

        Functions.showMsg(this, "Are you sure want to ignore this ?", new Functions.OnDialogButtonClickListener() {
            @Override
            public void onWhichClick(boolean click) {
                if (click) {
                    progressBar.showProgressDialog();
                    AppApi api = MyApplication.getRetrofit().create(AppApi.class);
                    Point point = new Point();
                    point.setPoint("-5");
                    point.setUserId(PrefUtils.getUserID(PopupActivity.this));
                    point.setTimestamp(Functions.getTimestamp());
                    Log.e("add point", MyApplication.getGson().toJson(point));
                    api.addPoint(point).enqueue(new Callback<BaseResponse<Point>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Point>> call, Response<BaseResponse<Point>> response) {
                            progressBar.hideProgressDialog();
                            if (response.body() != null && response.body().getStatus() == 1) {
                                Functions.fireIntent(PopupActivity.this, false);
                            } else {
                                Functions.showToast(PopupActivity.this, getString(R.string.try_again));
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Point>> call, Throwable t) {
                            progressBar.hideProgressDialog();
                            Functions.showToast(PopupActivity.this, getString(R.string.try_again));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isAccept)
            onBackPressed();
    }

    private void actionListener() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.showProgressDialog();
                AppApi api = MyApplication.getRetrofit().create(AppApi.class);
                Point point = new Point();
                point.setPoint("10");
                point.setUserId(PrefUtils.getUserID(PopupActivity.this));
                point.setTimestamp(Functions.getTimestamp());
                api.addPoint(point).enqueue(new Callback<BaseResponse<Point>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Point>> call, Response<BaseResponse<Point>> response) {
                        progressBar.hideProgressDialog();
                        if (response.body() != null && response.body().getStatus() == 1) {
                            isAccept = true;
                            Functions.openInMap(PopupActivity.this, latitude, longitude, Double.parseDouble(site.getLatitude()), Double.parseDouble(site.getLongitude()), site.getSite());
                            finish();
                        } else {
                            Functions.showToast(PopupActivity.this, getString(R.string.try_again));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Point>> call, Throwable t) {
                        progressBar.hideProgressDialog();
                        Functions.showToast(PopupActivity.this, getString(R.string.try_again));
                    }
                });
            }
        });
        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void init() {
        progressBar = new ProgressBarHelper(this, false);
        bottomView = (LinearLayout) findViewById(R.id.bottomView);
        btnIgnore = (TfButton) findViewById(R.id.btnIgnore);
        btnAccept = (TfButton) findViewById(R.id.btnAccept);
        txtDesc = (TfTextView) findViewById(R.id.txtDesc);
        txtSite = (TfTextView) findViewById(R.id.txtSite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);

        site = (Site) getIntent().getSerializableExtra("site");

        txtSite.setText(site.getSite());
        txtDesc.setText(site.getDescription());
        initToolbar();
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
}
