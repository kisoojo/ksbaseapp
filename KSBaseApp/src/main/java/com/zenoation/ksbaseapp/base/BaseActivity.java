package com.zenoation.ksbaseapp.base;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.zenoation.ksbaseapp.BuildConfig;
import com.zenoation.ksbaseapp.R;
import com.zenoation.ksbaseapp.utils.Utils;
import com.zenoation.ksbaseapp.utils.dialog.DialogUtils;
import com.zenoation.ksbaseapp.utils.pref.PrefUtils;

import static com.zenoation.ksbaseapp.BuildConfig.ANIM_KEY;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_BOTTOM;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_FADE;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_LEFT;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_NONE;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_NO_ANIM;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_RIGHT;
import static com.zenoation.ksbaseapp.base.BaseUtils.ANIM_TOP;
import static com.zenoation.ksbaseapp.base.BaseUtils.BUTTON_PRESS_DELAY;
import static com.zenoation.ksbaseapp.base.BaseUtils.FLAG_DEFAULT;
import static com.zenoation.ksbaseapp.base.BaseUtils.bytesToObject;

/**
 * Created by kisoojo on 2020.02.03
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private BaseUtils mBaseUtils;
    protected Utils mUtils;
    protected PrefUtils mPrefUtils;
    protected DialogUtils mDialogUtils;
    protected ContentValues mParams;
    private int mAnimType;
    private int mDelay = BUTTON_PRESS_DELAY;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication) getApplicationContext()).setCurrentActivity(this);

        mBaseUtils = BaseUtils.getInstance();
        mUtils = Utils.getInstance();
        mPrefUtils = PrefUtils.getInstance();
        mDialogUtils = DialogUtils.getInstance();

        setParams();
    }

    protected boolean hasParam(String key) {
        if (mParams == null) {
            return false;
        }

        try {
            Object o = mParams.get(key);
            return o != null;
        } catch (Exception e) {
            return false;
        }
    }

    protected Object getParam(String key) throws Exception {
        if (hasParam(key)) {
            return mParams.get(key);
        } else {
            return new Object();
        }
    }

    protected Object getParamObject(String key) throws Exception {
        if (hasParam(key)) {
            return bytesToObject((byte[]) mParams.get(key));
        } else {
            return new Object();
        }
    }

    private void setParams() {
        try {
            if (getIntent().hasExtra(BuildConfig.PARAMS_KEY)) {
                mParams = getIntent().getParcelableExtra(BuildConfig.PARAMS_KEY);
            } else {
                mParams = new ContentValues();
            }
        } catch (NullPointerException e) {
            mParams = new ContentValues();
        }

        try {
            if (getIntent().hasExtra(ANIM_KEY)) {
                mAnimType = getIntent().getIntExtra(ANIM_KEY, ANIM_NONE);
            } else {
                mAnimType = ANIM_NONE;
            }
        } catch (Exception e) {
            mAnimType = ANIM_NONE;
        }
    }

    protected void setInputDelay(int delay) {
        mDelay = delay;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setParams();
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            findViewById(R.id.iv_header_back).setOnClickListener(this);
            findViewById(R.id.iv_header_close).setOnClickListener(this);
        } catch (Exception e) {
            //do nothing...
        }
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        if (mDelay != 0) {
            mUtils.setPreventButtonClick(v, mDelay);
        }
        int id = v.getId();
        if (id == R.id.iv_header_back) {
            onBackPressed();
        } else if (id == R.id.iv_header_close) {
            finish();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //@Override
    //protected void attachBaseContext(Context newBase) {
    //    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    //}

    @CallSuper
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        ((BaseApplication) getApplicationContext()).onUserLeaveHint();
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        ((BaseApplication) getApplicationContext()).onActivityStart();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplicationContext()).setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void finish() {
        super.finish();
        mBaseUtils.finish(this, mAnimType);
    }

    protected void setHeaderTitle(@StringRes int title) {
        try {
            ((TextView) findViewById(R.id.tv_header_title)).setText(title);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderTitle(String title) {
        try {
            ((TextView) findViewById(R.id.tv_header_title)).setText(title);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderBack(boolean bIsVisible) {
        try {
            findViewById(R.id.iv_header_back).setVisibility(bIsVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderClose(boolean bIsVisible) {
        try {
            findViewById(R.id.iv_header_close).setVisibility(bIsVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            //do nothing...
        }
    }

    public void setAnim(int anim) {
        mBaseUtils.setAnim(this, anim);
    }

    public void startActivityAnim(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_RIGHT);
    }

    public void startActivity(Intent intent, int anim) {
        mBaseUtils.startActivity(this, intent, anim);
    }

    public void startActivityNoAnim(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_NO_ANIM);
    }

    public void startActivityLeft(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_LEFT);
    }

    public void startActivityRight(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_RIGHT);
    }

    public void startActivityTop(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_TOP);
    }

    public void startActivityBottom(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_BOTTOM);
    }

    public void startActivityFade(Intent intent) {
        mBaseUtils.startActivity(this, intent, ANIM_FADE);
    }

    public void finishNoAnim() {
        super.finish();
        mBaseUtils.finish(this, ANIM_NO_ANIM);
    }

    public void finishLeft() {
        super.finish();
        mBaseUtils.finish(this, ANIM_LEFT);
    }

    public void finishRight() {
        super.finish();
        mBaseUtils.finish(this, ANIM_RIGHT);
    }

    public void finishTop() {
        super.finish();
        mBaseUtils.finish(this, ANIM_TOP);
    }

    public void finishBottom() {
        super.finish();
        mBaseUtils.finish(this, ANIM_BOTTOM);
    }

    public void finishFade() {
        super.finish();
        mBaseUtils.finish(this, ANIM_FADE);
    }


    public void startActivity(Class target) {
        mBaseUtils.startActivity(this, target, FLAG_DEFAULT, null, ANIM_NONE, false);
    }

    public void startActivity(Class target, ContentValues params) {
        mBaseUtils.startActivity(this, target, FLAG_DEFAULT, params, ANIM_NONE, false);
    }

    public void startActivity(Class target, ContentValues params, int anim) {
        mBaseUtils.startActivity(this, target, FLAG_DEFAULT, params, anim, false);
    }

    public void startActivity(Class target, int flag) {
        mBaseUtils.startActivity(this, target, flag, null, ANIM_NONE, false);
    }

    public void startActivity(Class target, int flag, ContentValues params) {
        mBaseUtils.startActivity(this, target, flag, params, ANIM_NONE, false);
    }

    public void startActivity(Class target, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivity(this, target, flag, params, anim, false);
    }


    public void startActivityFinish(Class target) {
        mBaseUtils.startActivityFinish(this, target, FLAG_DEFAULT, null, ANIM_NONE);
    }

    public void startActivityFinish(Class target, ContentValues params) {
        mBaseUtils.startActivityFinish(this, target, FLAG_DEFAULT, params, ANIM_NONE);
    }

    public void startActivityFinish(Class target, ContentValues params, int anim) {
        mBaseUtils.startActivityFinish(this, target, FLAG_DEFAULT, params, anim);
    }

    public void startActivityFinish(Class target, int flag) {
        mBaseUtils.startActivityFinish(this, target, flag, null, ANIM_NONE);
    }

    public void startActivityFinish(Class target, int flag, ContentValues params) {
        mBaseUtils.startActivityFinish(this, target, flag, params, ANIM_NONE);
    }

    public void startActivityFinish(Class target, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivityFinish(this, target, flag, params, anim);
    }


    public void startActivityForResult(Class target, int reqId) {
        mBaseUtils.startActivityForResult(this, target, reqId, FLAG_DEFAULT, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, ContentValues params) {
        mBaseUtils.startActivityForResult(this, target, reqId, FLAG_DEFAULT, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(this, target, reqId, FLAG_DEFAULT, params, anim);
    }

    public void startActivityForResult(Class target, int reqId, int flag) {
        mBaseUtils.startActivityForResult(this, target, reqId, flag, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, int flag, ContentValues params) {
        mBaseUtils.startActivityForResult(this, target, reqId, flag, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(this, target, reqId, flag, params, anim);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
