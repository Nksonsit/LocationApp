package com.myapp.locationapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myapp.locationapp.R;
import com.myapp.locationapp.custom.TfButton;
import com.myapp.locationapp.custom.TfEditText;
import com.myapp.locationapp.custom.TfTextView;
import com.myapp.locationapp.helper.Functions;

public class AddDlUlActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private TfEditText edtDL;
    private TfEditText edtUL;
    private TfButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dl_ul);
        init();
        actionListener();
    }

    private void actionListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(AddDlUlActivity.this,view);
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

                edtDL.setText("");
                edtUL.setText("");
                Functions.showToast(AddDlUlActivity.this, "Successfully Uploaded");
            }
        });
    }

    private void init() {
        btnSubmit = (TfButton) findViewById(R.id.btnSubmit);
        edtUL = (TfEditText) findViewById(R.id.edtUL);
        edtDL = (TfEditText) findViewById(R.id.edtDL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
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

    @Override
    public void onBackPressed() {
        Functions.fireIntent(AddDlUlActivity.this, false);
    }
}
