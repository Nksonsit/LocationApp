package com.myapp.locationapp.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.myapp.locationapp.R;
import com.myapp.locationapp.api.AppApi;
import com.myapp.locationapp.custom.TfButton;
import com.myapp.locationapp.custom.TfEditText;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.AdvancedSpannableString;
import com.myapp.locationapp.helper.Functions;
import com.myapp.locationapp.helper.MyApplication;
import com.myapp.locationapp.helper.PrefUtils;
import com.myapp.locationapp.helper.ProgressBarHelper;
import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.FCM;
import com.myapp.locationapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private TfEditText edtFirstName;
    private TfEditText edtLastName;
    private TfEditText edtEmailId;
    private TfEditText edtPassword;
    private TfEditText edtConfirmPassword;
    private TfButton btnSignUp;
    private TfTextView txtSignIn;
    private CardView loginView;
    private ProgressBarHelper progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        actionListener();
    }

    private void actionListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(RegisterActivity.this, view);

                if (!Functions.isConnected(RegisterActivity.this)) {
                    Functions.showToast(RegisterActivity.this, getString(R.string.check_internet));
                }
                if (!Functions.isConnected(RegisterActivity.this)) {
                    Functions.showToast(RegisterActivity.this, getString(R.string.check_internet));
                    return;
                }
                if (edtFirstName.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, "Please enter your first name");
                    return;
                }

                if (edtLastName.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, "Please enter your last name");
                    return;
                }

                if (edtEmailId.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, "Please enter your email id");
                    return;
                }

                if (!Functions.emailValidation(edtEmailId.getText().toString().trim())) {
                    Functions.showToast(RegisterActivity.this, "Please enter your valid email id");
                    return;
                }

                if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, "Please enter password");
                    return;
                }

                if (edtPassword.getText().toString().trim().length() < 4 || edtPassword.getText().toString().trim().length() > 16) {
                    Functions.showToast(RegisterActivity.this, "Password should be minimum 4 and maximum 16 character long");
                    return;
                }

                if (edtConfirmPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, "Please enter confirm password");
                    return;
                }

                if (!edtPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
                    Functions.showToast(RegisterActivity.this, "Password and confirm password should be same");
                    return;
                }

                callApi();
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(RegisterActivity.this, view);
                Functions.fireIntent(RegisterActivity.this, LoginActivity.class, false);
                finish();
            }
        });
    }

    private void addFcm() {
        AppApi api = MyApplication.getRetrofit().create(AppApi.class);
        FCM fcm = new FCM();
        fcm.setUserId(PrefUtils.getUserID(this));
        if (PrefUtils.getFCMToken(this) == null || PrefUtils.getFCMToken(this).trim().length() == 0) {
            PrefUtils.setFCMToken(this, FirebaseInstanceId.getInstance().getToken());
        }
        fcm.setToken(PrefUtils.getFCMToken(this));
        Log.e("add fcm", MyApplication.getGson().toJson(fcm));
        api.addFcm(fcm).enqueue(new Callback<BaseResponse<FCM>>() {
            @Override
            public void onResponse(Call<BaseResponse<FCM>> call, Response<BaseResponse<FCM>> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse<FCM>> call, Throwable t) {

            }
        });
    }
    private void callApi() {
        User user = new User();
        user.setFirstName(edtFirstName.getText().toString().trim());
        user.setLastName(edtLastName.getText().toString().trim());
        user.setEmailId(edtEmailId.getText().toString().trim());
        user.setPassword(edtPassword.getText().toString().trim());
        user.setType(2);
        progressBar.showProgressDialog();
        Log.e("register req", MyApplication.getGson().toJson(user));
        AppApi api = MyApplication.getRetrofit().create(AppApi.class);
        api.getRegister(user).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null && response.body().getStatus() == 1) {
                    Log.e("register res", MyApplication.getGson().toJson(response.body()));
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        PrefUtils.setLoggedIn(RegisterActivity.this, true);
                        PrefUtils.setUserFullProfileDetails(RegisterActivity.this, response.body().getData().get(0));
                        Functions.fireIntent(RegisterActivity.this, MainActivity.class, true);
                        Functions.showToast(RegisterActivity.this,"Successfully Register");
                        addFcm();
                        finish();
                    } else {
                        Functions.showToast(RegisterActivity.this, "Email Id already exist");
                    }
                } else {
                    Functions.showToast(RegisterActivity.this, "Email Id already exist");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                progressBar.hideProgressDialog();
                Functions.showToast(RegisterActivity.this, "Something went wrong please try again later");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(RegisterActivity.this, LoginActivity.class, false);
        finish();
    }

    private void init() {
        progressBar = new ProgressBarHelper(this, false);
        loginView = (CardView) findViewById(R.id.loginView);
        txtSignIn = (TfTextView) findViewById(R.id.txtSignIn);
        btnSignUp = (TfButton) findViewById(R.id.btnSignUp);
        edtConfirmPassword = (TfEditText) findViewById(R.id.edtConfirmPassword);
        edtPassword = (TfEditText) findViewById(R.id.edtPassword);
        edtEmailId = (TfEditText) findViewById(R.id.edtEmailId);
        edtLastName = (TfEditText) findViewById(R.id.edtLastName);
        edtFirstName = (TfEditText) findViewById(R.id.edtFirstName);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);


        AdvancedSpannableString span = new AdvancedSpannableString("Welcome To Speed Catch");
        span.setBold("Speed Catch");
        span.setColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary), "Speed Catch");
        txtTitle.setText(span);
    }
}
