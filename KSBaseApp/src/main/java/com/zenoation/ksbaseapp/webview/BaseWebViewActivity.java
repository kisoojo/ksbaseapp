package com.zenoation.ksbaseapp.webview;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import com.zenoation.ksbaseapp.R;
import com.zenoation.ksbaseapp.base.BaseActivity;


/**
 * Created by kisoojo on 2020.06.04
 */
public class BaseWebViewActivity extends BaseActivity {

    protected WebView mWebView;
    protected Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initialize();
    }

    private void initialize() {
        mWebView = findViewById(R.id.wv_view);
        mBtn = findViewById(R.id.btn_confirm);
    }

    @Override
    public void onBackPressed() {
        try {
            if (mWebView.getChildCount() > 0) {
                WebView wv = (WebView) mWebView.getChildAt(mWebView.getChildCount() - 1);
                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    mWebView.removeView(wv);
                }
            } else {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }
}
