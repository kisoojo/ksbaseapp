package com.zenoation.ksbaseapp.webview;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.zenoation.ksbaseapp.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by kisoojo on 2020.03.17
 */
public class WebView extends android.webkit.WebView {

    private Context mContext;

    public WebView(Context context) {
        super(context);
        mContext = context;
        setWebView(this);
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setWebView(this);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWebView(this);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        setWebView(this);
    }

    public void setWebView(android.webkit.WebView webView) {
        WebViewClient webViewClient = new WebViewClient(mContext);
        WebChromeClient webChromeClient = new WebChromeClient((Activity) mContext, webView);

        webView.clearSslPreferences();
        webView.clearFormData();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearMatches();

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setTextZoom(100);
        // webView.addJavascriptInterface(new WebViewInterface(mContext), "WebViewInterface");
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {

                    // if (webViewClient.getCookie() == null) {
                    //     Utils.getInstance().showToast(mContext, "파일 다운로드 실패");
                    //     return;
                    // }

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);
                    request.addRequestHeader("cookie", webViewClient.getCookie());
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file");
                    String fileName = URLDecoder.decode(URLUtil.guessFileName(url, contentDisposition, mimetype), "EUC-KR").replace(";", "");
                    request.setTitle(fileName);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager dm = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Utils.getInstance().showToast(mContext, "다운로드를 시작합니다..");
                } catch (UnsupportedEncodingException e) {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        return;
                    }

                    if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        Utils.getInstance().showToast(mContext, "첨부파일 다운로드를 위해\n동의가 필요합니다.");
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                    }
                }
            }
        });
    }
}
