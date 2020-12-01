package com.zenoation.library.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zenoation.library.R;
import com.zenoation.library.listener.MyOnclickListener;


/**
 * Created by kisoojo on 2020.09.03
 */
public class WebViewActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = "";
        String url = "";
        String btnText = "";
        try {
            title = hasParam("title") ? (String) getParam("title") : "";
            url = hasParam("url") ? (String) getParam("url") : "";
            btnText = hasParam("btn_text") ? (String) getParam("btn_text") : "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
            mUtils.showToast(this, getString(R.string.wrong_access));
            finish();
            return;
        }

        setHeaderTitle(title);
        setHeaderBack(true);
        setHeaderClose(true);

        if (!TextUtils.isEmpty(btnText)) {
            mBtn.setVisibility(View.VISIBLE);
            mBtn.setText(btnText);
        }

        mWebView.loadUrl(url);

        mBtn.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                finish();
            }
        });
    }
}
