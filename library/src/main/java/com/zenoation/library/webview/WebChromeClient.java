package com.zenoation.library.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zenoation.library.utils.image.ImageUtils;

import java.io.File;
import java.io.IOException;

import static androidx.core.content.ContextCompat.getColor;


public class WebChromeClient extends android.webkit.WebChromeClient {
    private Activity mActivity;
    private WebView mWebView;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback mFilepathCallback;
    private String mCameraPhotoPath;
    private int mOriginalOrientation;
    private FrameLayout mFullscreenContainer;
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static int ScrollX = 0, ScrollY = 0;

    public WebChromeClient(Activity activity, WebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (!((Activity) view.getContext()).isFinishing()) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton("확인", (dialog, which) -> result.confirm())
                    .setCancelable(false)
                    .create()
                    .show();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (!((Activity) view.getContext()).isFinishing()) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton("확인", (dialog, which) -> result.confirm())
                    .setNegativeButton("취소", (dialog, which) -> result.cancel())
                    .setCancelable(false)
                    .create()
                    .show();
        }
        return true;
    }

    @Override
    public boolean onCreateWindow(WebView webView, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        com.zenoation.library.webview.WebView newWebView = new com.zenoation.library.webview.WebView(webView.getContext());
        newWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((com.zenoation.library.webview.WebView) webView).setWebView(newWebView);
        ScrollX = mWebView.getScrollX();
        ScrollY = mWebView.getScrollY();
        mWebView.scrollTo(0, 0);
        mWebView.addView(newWebView);
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
        mWebView.scrollTo(com.zenoation.library.webview.WebChromeClient.ScrollX, com.zenoation.library.webview.WebChromeClient.ScrollY);
        mWebView.removeView(window);
//        if (mWebView.getChildCount() == 0)
//            mWebView.setVisibility(View.GONE);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mOriginalOrientation = mActivity.getRequestedOrientation();
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        mFullscreenContainer = new FullscreenHolder(mActivity);
        mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
        mCustomView = view;
        setFullscreen(true);
        mCustomViewCallback = callback;
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onShowCustomView(view, callback);
    }


    // For Android Version < 3.0
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, 1000);
    }

    // For 3.0 <= Android Version < 4.1
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooser(uploadMsg, acceptType, "");
    }

    // For 4.1 <= Android Version < 5.0
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        Log.d(getClass().getName(), "openFileChooser : " + acceptType + "/" + capture);
        mUploadMessage = uploadFile;
        imageChooser();
    }

    //For Android 5.0+
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (mFilepathCallback != null) {
            mFilepathCallback.onReceiveValue(null);
        }
        mFilepathCallback = filePathCallback;
        imageChooser();
        return true;
    }

    private void imageChooser() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = ImageUtils.getInstance().makeCameraFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                Log.e(getClass().getName(), "Unable to create Image File", ex);
            }

            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "이미지 업로드");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        mActivity.startActivityForResult(chooserIntent, 1000);
    }


//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File imageFile = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        return imageFile;
//    }


    @SuppressWarnings("deprecation")
    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        this.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        }

        setFullscreen(false);
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCallback.onCustomViewHidden();
        mActivity.setRequestedOrientation(mOriginalOrientation);
    }

    private void setFullscreen(boolean enabled) {

        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (enabled) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
            if (mCustomView != null) {
                mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        win.setAttributes(winParams);
    }

    private static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(getColor(ctx, android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    public ValueCallback getFilepathCallback() {
        return mFilepathCallback;
    }

    public ValueCallback<Uri> getUploadMessage() {
        return mUploadMessage;
    }

    public String getCameraPhotoPath() {
        return mCameraPhotoPath;
    }

    public void setUploadMessage(ValueCallback<Uri> UploadMessage) {
        mUploadMessage = UploadMessage;
    }

    public void setFilepathCallback(ValueCallback FilepathCallback) {
        mFilepathCallback = FilepathCallback;
    }

    public void setonReceiveValue() {
        if (mFilepathCallback != null) {
            mFilepathCallback.onReceiveValue(null);
            mFilepathCallback = null;
        }
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mFilepathCallback = null;
        }
    }
}
