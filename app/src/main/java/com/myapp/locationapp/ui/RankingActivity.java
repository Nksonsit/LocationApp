package com.myapp.locationapp.ui;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.myapp.locationapp.R;
import com.myapp.locationapp.adapter.RankingAdapter;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.Point;
import com.myapp.locationapp.model.PointReq;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TfTextView txtAlert;
    private RankingAdapter adapter;
    private List<Point> list;
    private ProgressBarHelper progressBar;
    private RelativeLayout topView;
    private TfTextView txtStartDate;
    private TfTextView txtEndDate;
    private Calendar now = Calendar.getInstance();
    private DatePickerDialog dpd;
    private boolean isStartData = false;
    private String startDate, endDate;
    private ImageView imgRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        init();
        actionListener();
    }

    private void actionListener() {
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartData = true;
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartData = false;
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(imgRefresh, "rotation", 0f, 360f);
                imageViewObjectAnimator.setDuration(500);
                imageViewObjectAnimator.start();
                if (Functions.isConnected(RankingActivity.this)) {
                    callApi();
                } else {
                    Functions.showToast(RankingActivity.this, getString(R.string.check_internet));
                }
            }
        });
    }

    private void init() {
        topView = (RelativeLayout) findViewById(R.id.topView);
        dpd = DatePickerDialog.newInstance(
                RankingActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
        txtStartDate = (TfTextView) findViewById(R.id.txtStartDate);
        txtEndDate = (TfTextView) findViewById(R.id.txtEndDate);
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


        PointReq pointReq = new PointReq();
        if (startDate != null && startDate.trim().length() > 0) {
            pointReq.setStartDate(startDate);
        }
        if (endDate != null && endDate.trim().length() > 0) {
            pointReq.setEndDate(endDate);
        }

        AppApi api= MyApplication.getRetrofit().create(AppApi.class);
        api.getPoint(pointReq).enqueue(new Callback<BaseResponse<Point>>() {
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = (monthOfYear+1) + "";
        String day = dayOfMonth + "";
        if (month.trim().length() == 1) {
            month = "0" + month;
        }
        if (day.trim().length() == 1) {
            day = "0" + day;
        }
        if (isStartData) {
            startDate = year + "" + month + "" + day;
            txtStartDate.setText(day + " / " + month + " / " + year);
        } else {
            if(Integer.parseInt(startDate) <= Integer.parseInt(year + "" + month + "" + day) ){
                endDate = year + "" + month + "" + day;
                txtEndDate.setText(day + " / " + month + " / " + year);}else{
                Functions.showToast(RankingActivity.this,"Please select valid date");
            }
        }
        isStartData = false;
    }
}
