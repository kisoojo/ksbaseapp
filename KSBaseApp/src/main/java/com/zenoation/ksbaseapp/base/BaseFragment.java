package com.zenoation.ksbaseapp.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private  BaseUtils mBaseUtils;
    protected Utils mUtils;
    protected PrefUtils mPrefUtils;
    protected DialogUtils mDialogUtils;
    protected ContentValues mParams;
    private int mAnimType;
    protected int mDelay = BUTTON_PRESS_DELAY;
    protected Activity mActivity;

    protected View mFragmentView;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            if (getArguments() != null && getArguments().containsKey(BuildConfig.PARAMS_KEY)) {
                mParams = getArguments().getParcelable(BuildConfig.PARAMS_KEY);
            } else {
                mParams = new ContentValues();
            }
        } catch (NullPointerException e) {
            mParams = new ContentValues();
        }

        try {
            if (getArguments() != null && getArguments().containsKey(ANIM_KEY)) {
                mAnimType = getArguments().getInt(ANIM_KEY, ANIM_NONE);
            } else {
                mAnimType = ANIM_NONE;
            }
        } catch (Exception e) {
            mAnimType = ANIM_NONE;
        }
    }

    public void refresh(ContentValues params) {
        mParams = params;
    }

    public boolean isDestroyed() {
        return mFragmentView == null;
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        if (mDelay != 0) {
            mUtils.setPreventButtonClick(v, mDelay);
        }
        int id = v.getId();
        if (id == R.id.iv_header_back) {
            mActivity.onBackPressed();
        } else if (id == R.id.iv_header_close) {
            mActivity.finish();
        }
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
    }

    @CallSuper
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        } else {
            Activity activity = ((BaseApplication) context.getApplicationContext()).getCurrentActivity();
            if (activity != null) {
                mActivity = activity;
            }
        }
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Dlog.d("onViewStateRestored");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    abstract protected void initialize();

    protected void setHeaderTitle(@StringRes int title) {
        try {
            ((TextView) mFragmentView.findViewById(R.id.tv_header_title)).setText(title);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderTitle(String title) {
        try {
            ((TextView) mFragmentView.findViewById(R.id.tv_header_title)).setText(title);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderView() {
        try {
            mFragmentView.findViewById(R.id.iv_header_back).setOnClickListener(this);
            mFragmentView.findViewById(R.id.iv_header_close).setOnClickListener(this);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderBack(boolean bIsVisible) {
        try {
            mFragmentView.findViewById(R.id.iv_header_back).setVisibility(bIsVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            //do nothing...
        }
    }

    protected void setHeaderClose(boolean bIsVisible) {
        try {
            mFragmentView.findViewById(R.id.iv_header_close).setVisibility(bIsVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            //do nothing...
        }
    }

    public void setAnim(int anim) {
        mBaseUtils.setAnim(mActivity, anim);
    }

    private void setAnimFrag(FragmentTransaction tr, int anim) {
        mBaseUtils.setAnimFrag(tr, anim);
    }

    protected void setFragmentParent(int id, Fragment fragment, boolean isBackStack) {
        ((BaseFragmentActivity) mActivity).setFragment(id, fragment, true);
    }


    protected void setFragmentParent(int id, Fragment fragment, boolean isBackStack, int anim) {
        ((BaseFragmentActivity) mActivity).setFragment(id, fragment, true, anim);
    }

    protected void setFragment(int id, Fragment fragment, boolean isBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //setFragAnimationFade(transaction);
        transaction.replace(id, fragment);
        if (isBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    protected void setFragment(int id, Fragment fragment, boolean isBackStack, int animType) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle bundle;
        if (fragment.getArguments() != null) {
            bundle = fragment.getArguments();
        } else {
            bundle = new Bundle();
        }
        bundle.putInt(ANIM_KEY, animType);
        fragment.setArguments(bundle);
        setAnimFrag(transaction, animType);

        transaction.replace(id, fragment);
        if (isBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    public void setFragAnimationNoAnim(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_NO_ANIM);
    }

    public void setFragAnimationLeft(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_LEFT);
    }

    public void setFragAnimationRight(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_RIGHT);
    }

    public void setFragAnimationTop(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_TOP);
    }

    public void setFragAnimationBottom(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_BOTTOM);
    }

    public void setFragAnimationFade(FragmentTransaction tr) {
        mBaseUtils.setAnimFrag(tr, ANIM_FADE);
    }

    public void startActivityAnim(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_RIGHT);
    }

    public void startActivity(Intent intent, int anim) {
        mBaseUtils.startActivity(mActivity, intent, anim);
    }

    public void startActivityNoAnim(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_NO_ANIM);
    }

    public void startActivityLeft(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_LEFT);
    }

    public void startActivityRight(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_RIGHT);
    }

    public void startActivityTop(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_TOP);
    }

    public void startActivityBottom(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_BOTTOM);
    }

    public void startActivityFade(Intent intent) {
        mBaseUtils.startActivity(mActivity, intent, ANIM_FADE);
    }

    public void finishNoAnim() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_NO_ANIM);
    }

    public void finishLeft() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_LEFT);
    }

    public void finishRight() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_RIGHT);
    }

    public void finishTop() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_TOP);
    }

    public void finishBottom() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_BOTTOM);
    }

    public void finishFade() {
        mActivity.finish();
        mBaseUtils.finish(mActivity, ANIM_FADE);
    }

    public void startActivityAnim(Class target) {
        mBaseUtils.startActivity(mActivity, target, FLAG_DEFAULT, null, ANIM_RIGHT, false);
    }

    public void startActivity(Class target) {
        mBaseUtils.startActivity(mActivity, target, FLAG_DEFAULT, null, ANIM_NONE, false);
    }

    public void startActivity(Class target, ContentValues params) {
        mBaseUtils.startActivity(mActivity, target, FLAG_DEFAULT, params, ANIM_NONE, false);
    }

    public void startActivity(Class target, ContentValues params, int anim) {
        mBaseUtils.startActivity(mActivity, target, FLAG_DEFAULT, params, anim, false);
    }

    public void startActivity(Class target, int flag) {
        mBaseUtils.startActivity(mActivity, target, flag, null, ANIM_NONE, false);
    }

    public void startActivity(Class target, int flag, ContentValues params) {
        mBaseUtils.startActivity(mActivity, target, flag, params, ANIM_NONE, false);
    }

    public void startActivity(Class target, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivity(mActivity, target, flag, params, anim, false);
    }


    public void startActivityFinish(Class target) {
        mBaseUtils.startActivityFinish(mActivity, target, FLAG_DEFAULT, null, ANIM_NONE);
    }

    public void startActivityFinish(Class target, ContentValues params) {
        mBaseUtils.startActivityFinish(mActivity, target, FLAG_DEFAULT, params, ANIM_NONE);
    }

    public void startActivityFinish(Class target, ContentValues params, int anim) {
        mBaseUtils.startActivityFinish(mActivity, target, FLAG_DEFAULT, params, anim);
    }

    public void startActivityFinish(Class target, int flag) {
        mBaseUtils.startActivityFinish(mActivity, target, flag, null, ANIM_NONE);
    }

    public void startActivityFinish(Class target, int flag, ContentValues params) {
        mBaseUtils.startActivityFinish(mActivity, target, flag, params, ANIM_NONE);
    }

    public void startActivityFinish(Class target, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivityFinish(mActivity, target, flag, params, anim);
    }


    public void startActivityForResult(Class target, int reqId) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, FLAG_DEFAULT, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, ContentValues params) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, FLAG_DEFAULT, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, FLAG_DEFAULT, params, anim);
    }

    public void startActivityForResult(Class target, int reqId, int flag) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, flag, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, int flag, ContentValues params) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, flag, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, int reqId, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(mActivity, target, reqId, flag, params, anim);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, FLAG_DEFAULT, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId, ContentValues params) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, FLAG_DEFAULT, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, FLAG_DEFAULT, params, anim);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId, int flag) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, flag, null, ANIM_NONE);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId, int flag, ContentValues params) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, flag, params, ANIM_NONE);
    }

    public void startActivityForResult(Class target, Fragment fragment, int reqId, int flag, ContentValues params, int anim) {
        mBaseUtils.startActivityForResult(mActivity, fragment, target, reqId, flag, params, anim);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
