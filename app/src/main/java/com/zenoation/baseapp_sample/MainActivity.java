package com.zenoation.baseapp_sample;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.zenoation.library.base.BaseActivity;
import com.zenoation.library.listener.OnCompleteParamListener;
import com.zenoation.library.utils.image.ImageUtils;
import com.zenoation.library.webview.WebViewActivity;

/**
 * Created by kisoojo on 2020.11.27
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showConfirmDialog(MainActivity.this,
                        "테스트",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialogUtils.showAlertDialog(MainActivity.this, "테스트");
                            }
                        });
            }
        });

        findViewById(R.id.btn_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues params = new ContentValues();
                params.put("title", "타이틀");
                params.put("url", "http://naver.com");
                params.put("btn_text", "종료");
                startActivity(WebViewActivity.class, params);
            }
        });

        ImageUtils.getInstance().setOnCameraCompleteListener(new OnCompleteParamListener() {
            @Override
            public void onComplete(Object param) {
                ContentValues params = (ContentValues) param;
                mDialogUtils.showAlertDialog(MainActivity.this,
                        "path: " + params.get("path") + "\n" +
                                "file: " + params.get("name")
                );
            }
        });

        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showImageFileDialog(MainActivity.this,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageUtils.getInstance().doCamera(MainActivity.this);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageUtils.getInstance().doGallery(MainActivity.this);
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageUtils.CALL_CAMERA) {
                ImageUtils.getInstance().setImageFile(this, ImageUtils.CALL_CAMERA, "", "");
            } else if (requestCode == ImageUtils.CALL_GALLERY) {
                if (data != null) {
                    if (data.getClipData() != null) {
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            ClipData.Item item = data.getClipData().getItemAt(i);
                            ImageUtils.getInstance().makeGalleryFile(this, item.getUri());
                        }
                    } else if (data.getData() != null) {
                        ImageUtils.getInstance().makeGalleryFile(this, data.getData());
                    }
                }
            }
        }
    }
}
