package com.zenoation.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * Created by kisoojo on 2020.02.03
 */

public class BaseApplication extends MultiDexApplication {

    private final static String TAG = "MainApplication";

    public final static String TEMP_PATH = Environment.getExternalStorageDirectory() + File.separator + "onvitplatform";

    public static String mSimpleName;

    //어플 백그라운드 진입 여부
    private boolean mIsAppWentToBack = false;
    //현재 액티비티 저장
    private Activity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        //     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectNonSdkApiUsage().build());
        // }
    }

    /**
     * Activity 가 백그라운드 상태로 진입하기 직전 onPause() 앞에서 호출된다.
     * 홈키는 입력 이벤트가 전달되지 않아 백그라운드 진입을 판단하기 어려운데
     * 이 메소드의 호출로 백그라운드 진입인지 아닌지를 판단할 수 있다.
     * 각 Activity 마다 onUserLeaveHint() 를 Override 한 후 이 메소드를 호출하도록 하고,
     * mIsAppWentToBack 변수로 상태를 저장한다.
     * 액티비티 실행시 이 메소드가 호출되지 않게 하려면 FLAG_ACTIVITY_NO_USER_ACTION 플래그를 추가한다.
     */
    public void onUserLeaveHint() {
        if (!mIsAppWentToBack) {
            mIsAppWentToBack = true;
            Log.d(TAG, "AppWentToBack");
        }
    }

    /**
     * 각 Activity 에서 onStart() 를 Override 한 후 이 메소드를 호출하도록 한다.
     * 어플이 백그라운드 진입 후 다시 활성화 되는 경우 잠금화면을 표시하도록 하는 역할을 한다.
     */
    public void onActivityStart() {
        if (mIsAppWentToBack) {
            mIsAppWentToBack = false;
            Log.d(TAG, "AppWentToFront");
        }
    }

    /**
     * 백그라운드 진입 여부
     */
    public boolean isAppWentToBack() {
        return mIsAppWentToBack;
    }

    public Context getGlobalApplicationContext() {
        return this;
    }

    /**
     * Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
     */
    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
        if (mCurrentActivity != null) {
            mSimpleName = currentActivity.getClass().getSimpleName();
        }
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public Context getCurrentContext() {
        return mCurrentActivity != null ? mCurrentActivity : this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "MainApplication onTerminate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "MainApplication onLowMemory");
    }


    /**
     * UncaughtException 발생 시 앱 재시작 등록
     */
    private void setUncaughtExceptionHandler() {
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            // Chain default exception handler.
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, exception);
            }

            Log.e(TAG, exception.getMessage());

            //어플 재시작
            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
