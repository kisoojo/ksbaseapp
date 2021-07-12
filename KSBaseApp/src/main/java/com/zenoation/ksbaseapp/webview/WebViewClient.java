package com.zenoation.ksbaseapp.webview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.zenoation.ksbaseapp.BuildConfig;
import com.zenoation.ksbaseapp.utils.dialog.DialogUtils;

import java.net.URISyntaxException;

public class WebViewClient extends android.webkit.WebViewClient {
    private final static String TAG = "WebViewClient";

    protected Context mContext;
    private String cookie;
    android.webkit.CookieManager cm;
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";
    public static final String INTENT_PROTOCOL_END = ";end;";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_START = "intent:";

    public WebViewClient(Context context) {
        this.mContext = context;
        cm = android.webkit.CookieManager.getInstance();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        cookie = cm.getCookie(url);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.putExtra("com.android.browser.application_id", mContext.getPackageName());
        if (url.startsWith("sms:")) {
            mContext.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(url)));
            return true;
        }
        if (url.startsWith("tel:")) {
            mContext.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
            return true;
        }
        if (url.startsWith("mailto:")) {
            mContext.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(url)));
            return true;
        }
        if (url.startsWith("intent:")) {
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                mContext.startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (url.startsWith("market:")) {
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    mContext.startActivity(intent);
                }
                return true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (url.startsWith("onvitplatform:")) {
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    mContext.startActivity(intent);
                }
                return true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (url.startsWith("onvit:")) {
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    mContext.startActivity(intent);
                }
                return true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        if (!url.startsWith(INTENT_PROTOCOL_START)) {
            return false;
        }
        int customUrlStartIndex = INTENT_PROTOCOL_START.length();
        int customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT);
        if (customUrlEndIndex < 0) {
            return false;
        }
        try {
            mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url.substring(customUrlStartIndex, customUrlEndIndex))));
        } catch (ActivityNotFoundException e2) {
            int packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length();
            int packageEndIndex = url.indexOf(INTENT_PROTOCOL_END);
            if (packageEndIndex < 0) {
                packageEndIndex = url.length();
            }
            mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(new StringBuilder(GOOGLE_PLAY_STORE_PREFIX).append(url.substring(packageStartIndex, packageEndIndex)).toString())));
        }
        view.loadUrl(url);
        return true;
    }

    public String getCookie() {
        if (!TextUtils.isEmpty(cookie)) {
            return cookie;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        switch (error.getErrorCode()) {
            case ERROR_AUTHENTICATION:          // 서버에서 사용자 인증 실패
                break;

            case ERROR_BAD_URL:                 // 잘못된 URL
                break;

            case ERROR_CONNECT:                 // 서버로 연결 실패
                break;

            case ERROR_FAILED_SSL_HANDSHAKE:    // SSL handshake 수행 실패
                break;

            case ERROR_FILE:                    // 일반 파일 오류
                break;

            case ERROR_FILE_NOT_FOUND:          // 파일을 찾을 수 없습니다
                break;

            case ERROR_HOST_LOOKUP:             // 서버 또는 프록시 호스트 이름 조회 실패
                break;

            case ERROR_IO:                      // 서버에서 읽거나 서버로 쓰기 실패
                break;

            case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                break;

            case ERROR_REDIRECT_LOOP:           // 너무 많은 리디렉션
                break;

            case ERROR_TIMEOUT:                 // 연결 시간 초과
                break;

            case ERROR_TOO_MANY_REQUESTS:       // 페이지 로드중 너무 많은 요청 발생
                break;

            case ERROR_UNKNOWN:                 // 일반 오류
                break;

            case ERROR_UNSUPPORTED_AUTH_SCHEME: // 지원되지 않는 인증 체계
                break;

            case ERROR_UNSUPPORTED_SCHEME:      // URI가 지원되지 않는 방식
                break;
        }

        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        Log.w(TAG, "SSL Error " + error.getPrimaryError());

        if (BuildConfig.DEBUG) {
            handler.proceed();
        } else {
            StringBuilder message = new StringBuilder();
            switch (error.getPrimaryError()) {
                case SslError.SSL_EXPIRED:
                    message.append("이 사이트의 보안 인증서가 만료되었습니다.\n");
                    break;
                case SslError.SSL_IDMISMATCH:
                    message.append("이 사이트의 보안 인증서 ID가 일치하지 않습니다.\n");
                    break;
                case SslError.SSL_NOTYETVALID:
                    message.append("이 사이트의 보안 인증서가 아직 유효하지 않습니다.\n");
                    break;
                case SslError.SSL_UNTRUSTED:
                    message.append("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n");
                    break;
                default:
                    message.append("보안 인증서에 오류가 있습니다.\n");
                    break;
            }
            message.append("계속 진행하시겠습니까?");

            DialogUtils.getInstance().showConfirmDialog(
                    (Activity) mContext,
                    "알림",
                    message.toString(),
                    "진행",
                    "취소",
                    v -> handler.proceed(),
                    v -> {
                        handler.cancel();
                        ((Activity) mContext).finish();
                    }, false);
        }
    }
}
