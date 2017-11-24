package com.myapp.locationapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.myapp.locationapp.R;
import com.myapp.locationapp.helper.Functions;

/**
 * Created by ishan on 22-11-2017.
 */

public class AddNewsDialog extends Dialog {
    private OnAddNews onAddNews;
    private Context context;
    private View view;
    private TfEditText edtNews;
    private TfButton btnAdd;

    public AddNewsDialog(@NonNull Context context, OnAddNews onAddNews) {
        super(context);
        this.context = context;
        this.onAddNews = onAddNews;
        init();
        show();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_add_news, null);
        setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);

        edtNews = (TfEditText) view.findViewById(R.id.edtNews);
        btnAdd = (TfButton) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, context.getString(R.string.check_internet));
                    return;
                }
                if (edtNews.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter news headline");
                    return;
                }
                onAddNews.onAddNews(edtNews.getText().toString().trim());
                dismiss();
            }
        });
    }

    public interface OnAddNews {
        void onAddNews(String trim);
    }
}
