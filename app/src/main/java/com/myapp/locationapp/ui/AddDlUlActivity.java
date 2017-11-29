package com.myapp.locationapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.gun0912.tedpermission.PermissionListener;
import com.myapp.locationapp.R;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.TfButton;
import com.myapp.locationapp.custom.TfEditText;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.GPSTracker;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.DlUl;
import com.myapp.locationapp.model.Site;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDlUlActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private TfEditText edtDL;
    private TfEditText edtUL;
    private TfButton btnSubmit;
    private String TAG = "AddDlUlActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private double latitude = 0;
    private double longitude = 0;
    private ProgressBarHelper progressBar;
    private Site site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dl_ul);

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Functions.setPermission(AddDlUlActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        displayLocationSettingsRequest(AddDlUlActivity.this);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Functions.showToast(AddDlUlActivity.this, "You have denied service");
                        onBackPressed();
                    }
                });
            }
        }.start();
    }

    private void actionListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(AddDlUlActivity.this, view);
                if (!Functions.isConnected(AddDlUlActivity.this)) {
                    Functions.showToast(AddDlUlActivity.this, getString(R.string.check_internet));
                    return;
                }
                if (edtDL.getText().toString().trim().length() == 0) {
                    Functions.showToast(AddDlUlActivity.this, "Please enter DL");
                    return;
                }

                if (edtUL.getText().toString().trim().length() == 0) {
                    Functions.showToast(AddDlUlActivity.this, "Please enter UL");
                    return;
                }

                DlUl dlUl = new DlUl();
                dlUl.setDl(edtDL.getText().toString().trim());
                dlUl.setUl(edtUL.getText().toString().trim());
                dlUl.setLatitude(latitude + "");
                dlUl.setLongitude(longitude + "");
                dlUl.setUserId(PrefUtils.getUserID(AddDlUlActivity.this));
                dlUl.setSiteId(site.getId());
                dlUl.setTimestamp(Functions.getTimestamp());

                addDlUl(dlUl);

            }
        });
    }

    private void addDlUl(DlUl dlUl) {
        progressBar.showProgressDialog();
        Log.e("add dlul req", MyApplication.getGson().toJson(dlUl));
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.addDlUl(dlUl).enqueue(new Callback<BaseResponse<DlUl>>() {
            @Override
            public void onResponse(Call<BaseResponse<DlUl>> call, Response<BaseResponse<DlUl>> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null && response.body().getStatus() == 1) {
                    Log.e("add dlul res", MyApplication.getGson().toJson(response.body()));
                    edtDL.setText("");
                    edtUL.setText("");
                    Functions.showToast(AddDlUlActivity.this, "Successfully Uploaded");
                    onBackPressed();
                } else {
                    Functions.showToast(AddDlUlActivity.this, getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DlUl>> call, Throwable t) {
                progressBar.hideProgressDialog();
                Functions.showToast(AddDlUlActivity.this, getString(R.string.try_again));
            }
        });
    }

    private void init() {
        site= (Site)getIntent().getSerializableExtra("site");
        progressBar = new ProgressBarHelper(this, false);
        btnSubmit = (TfButton) findViewById(R.id.btnSubmit);
        edtUL = (TfEditText) findViewById(R.id.edtUL);
        edtDL = (TfEditText) findViewById(R.id.edtDL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        initToolbar();
        actionListener();

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            latitude=gpsTracker.getLatitude();
            longitude=gpsTracker.getLongitude();
            Log.e("lat long", gpsTracker.getLatitude() + " " + gpsTracker.getLongitude());
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
        Functions.fireIntent(AddDlUlActivity.this, false);
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        init();
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(AddDlUlActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            init();
        }
    }
}
