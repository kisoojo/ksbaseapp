package com.zenoation.library.listener;

import android.view.View;

import androidx.annotation.CallSuper;

import com.zenoation.library.utils.Utils;

import static com.zenoation.library.base.BaseUtils.BUTTON_PRESS_DELAY;

/**
 * Created by kisoojo on 2020.02.03
 */

public class MyOnclickListener implements View.OnClickListener {

    @CallSuper
    @Override
    public void onClick(View v) {
        Utils.getInstance().setPreventButtonClick(v, BUTTON_PRESS_DELAY);
    }
}