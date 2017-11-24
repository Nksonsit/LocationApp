package com.myapp.locationapp.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.myapp.locationapp.R;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private TfTextView speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        speed = (TfTextView) findViewById(R.id.speed);

        speed.setTypeface(Typeface.createFromAsset(getResources().getAssets(),"fonts/speed2.ttf"));

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (PrefUtils.isUserLoggedIn(SplashActivity.this)) {
                    Functions.fireIntent(SplashActivity.this, MainActivity.class, true);
                } else {
                    Functions.fireIntent(SplashActivity.this, LoginActivity.class, true);
                }
                finish();
            }
        }.start();
    }
}
