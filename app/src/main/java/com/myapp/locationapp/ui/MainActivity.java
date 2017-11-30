package com.myapp.locationapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.MyService;

import java.util.ArrayList;

import static com.myapp.locationapp.ui.AddDlUlActivity.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private RelativeLayout btnNewsHeadline;
    private RelativeLayout btnLink;
    private RelativeLayout btnRanking;
    private ImageView imgLogout;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Functions.setPermission(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        displayLocationSettingsRequest(MainActivity.this);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Functions.showToast(MainActivity.this, "You have denied service");
                        onBackPressed();
                    }
                });
            }
        }.start();
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
                        actionListener();
                        Log.e(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void actionListener() {
        btnNewsHeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this, view);

                if (!Functions.isConnected(MainActivity.this)) {
                    Functions.showToast(MainActivity.this, getString(R.string.check_internet));
                    return;
                }
                Functions.fireIntent(MainActivity.this, NewsActivity.class, true);
            }
        });
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this, view);

                if (!Functions.isConnected(MainActivity.this)) {
                    Functions.showToast(MainActivity.this, getString(R.string.check_internet));
                    return;
                }
                Functions.fireIntent(MainActivity.this, SiteActivity.class, true);

            }
        });
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(MainActivity.this, view);
                if (!Functions.isConnected(MainActivity.this)) {
                    Functions.showToast(MainActivity.this, getString(R.string.check_internet));
                    return;
                }
                Functions.fireIntent(MainActivity.this, RankingActivity.class, true);

            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.showMsg(MainActivity.this, "Are you sure want to logout ?", new Functions.OnDialogButtonClickListener() {
                    @Override
                    public void onWhichClick(boolean click) {
                        if (click) {
                            Functions.logout(MainActivity.this);
                        }
                    }
                });
            }
        });
    }

    private void init() {
        btnRanking = (RelativeLayout) findViewById(R.id.btnRanking);
        btnLink = (RelativeLayout) findViewById(R.id.btnLink);
        btnNewsHeadline = (RelativeLayout) findViewById(R.id.btnNewsHeadline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgLogout = (ImageView) findViewById(R.id.imgLogout);
        txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/speed2.ttf"));

        initToolbar();


        startService(new Intent(this, MyService.class));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setTitle("");
    }


}
