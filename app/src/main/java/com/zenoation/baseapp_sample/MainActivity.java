package com.zenoation.baseapp_sample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.zenoation.library.base.BaseActivity;

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

    }
}
