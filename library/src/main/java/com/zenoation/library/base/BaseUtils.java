package com.zenoation.library.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.zenoation.library.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static com.zenoation.library.BuildConfig.ANIM_KEY;
import static com.zenoation.library.BuildConfig.PARAMS_KEY;


/**
 * Created by kisoojo on 2020.02.03
 */

public class BaseUtils {

    public final static int FLAG_NONE = -1;
    public final static int FLAG_DEFAULT = 1;
    public final static int FLAG_CLEAR_SINGLE = 2;

    public final static int ANIM_NONE = -1;
    public final static int ANIM_NO_ANIM = 0;
    public final static int ANIM_LEFT = 1;
    public final static int ANIM_RIGHT = 2;
    public final static int ANIM_TOP = 3;
    public final static int ANIM_BOTTOM = 4;
    public final static int ANIM_FADE = 5;

    public final static int BUTTON_PRESS_DELAY = 500;

    //private static class LazyHolder {
    //    private static final BaseUtils INSTANCE = new BaseUtils();
    //}
    //
    //public static BaseUtils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static BaseUtils uniqueInstance;

    public static BaseUtils getInstance() {
        if (uniqueInstance == null) {
            synchronized (BaseUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new BaseUtils();
                }
            }
        }
        return uniqueInstance;
    }

    private BaseUtils() {
    }

    public void startActivity(Activity activity, Intent intent, int anim) {
        intent.putExtra(ANIM_KEY, anim);
        activity.startActivity(intent);
        setAnim(activity, anim);
    }

    public void startActivity(Activity activity, Class target) {
        startActivity(activity, target, FLAG_DEFAULT, null, ANIM_NONE, false);
    }

    public void startActivity(Activity activity, Class target, int flag) {
        startActivity(activity, target, flag, null, ANIM_NONE, false);
    }

    public void startActivity(Activity activity, Class target, int flag, ContentValues params) {
        startActivity(activity, target, flag, params, ANIM_NONE, false);
    }

    public void startActivityFinish(Activity activity, Class target, int flag, ContentValues params, int animType) {
        startActivity(activity, target, flag, params, animType, true);
    }

    private int mDoingStartActivityLoopCnt = 0;

    public void startActivity(Activity activity, Class target, int flag, ContentValues params, int animType, boolean isFinish) {
        Intent intent = new Intent(activity, target);
        if (flag == FLAG_DEFAULT) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else if (flag == FLAG_NONE) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else {
            intent.setFlags(flag | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        }
        if (params != null) {
            intent.putExtra(PARAMS_KEY, params);
        }
        if (animType >= ANIM_NO_ANIM && animType <= ANIM_FADE) {
            intent.putExtra(ANIM_KEY, animType);
        }

        while (mDoingStartActivityLoopCnt < 3) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDoingStartActivityLoopCnt++;
        }

        //다이얼로그 있는 상태에서 바로 액티비티 실행할 경우 애니메이션 무시되는 현상 때문에 딜레이 적용
        new Handler().postDelayed(() -> {
            activity.startActivity(intent);
            setAnim(activity, animType);
            if (isFinish) {

                //바로 종료 할 경우 애니메이션 무시되는 현상 때문에 딜레이 적용
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                        mDoingStartActivityLoopCnt = 0;
                    }
                }, 200);
            }
        }, 100);

        new Handler().postDelayed(() -> mDoingStartActivityLoopCnt = 0, 300);
    }

    public void startActivityForResult(Activity activity, Class target, int reqId, int flag, ContentValues params, int animType) {
        Intent intent = new Intent(activity, target);
        if (flag == FLAG_DEFAULT) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else if (flag == FLAG_CLEAR_SINGLE) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else if (flag != FLAG_NONE) {
            intent.setFlags(flag | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        }
        if (params != null) {
            intent.putExtra(PARAMS_KEY, params);
        }
        if (animType >= ANIM_NO_ANIM && animType <= ANIM_FADE) {
            intent.putExtra(ANIM_KEY, animType);
        }

        if (mDoingStartActivityLoopCnt > 0) {
            return;
        }

        //다이얼로그 있는 상태에서 바로 액티비티 실행할 경우 애니메이션 무시되는 현상 때문에 딜레이 적용
        new Handler().postDelayed(() -> {
            activity.startActivityForResult(intent, reqId);
            setAnim(activity, animType);
        }, 100);

        new Handler().postDelayed(() -> mDoingStartActivityLoopCnt = 0,300);
    }

    public void startActivityForResult(Activity activity, Fragment fragment, Class target, int reqId, int flag, ContentValues params, int animType) {
        Intent intent = new Intent(activity, target);
        if (flag == FLAG_DEFAULT) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else if (flag == FLAG_CLEAR_SINGLE) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        } else if (flag != FLAG_NONE) {
            intent.setFlags(flag | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        }
        if (params != null) {
            intent.putExtra(PARAMS_KEY, params);
        }
        if (animType >= ANIM_NO_ANIM && animType <= ANIM_FADE) {
            intent.putExtra(ANIM_KEY, animType);
        }

        if (mDoingStartActivityLoopCnt > 0) {
            return;
        }

        //다이얼로그 있는 상태에서 바로 액티비티 실행할 경우 애니메이션 무시되는 현상 때문에 딜레이 적용
        new Handler().postDelayed(() -> {
            fragment.startActivityForResult(intent, reqId);
            setAnim(activity, animType);
        }, 100);

        new Handler().postDelayed(() -> mDoingStartActivityLoopCnt = 0,300);
    }

    public void setAnim(Activity activity, int animType) {
        if (animType == ANIM_NO_ANIM) {
            activity.overridePendingTransition(0, 0);
        } else if (animType == ANIM_LEFT) {
            activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (animType == ANIM_RIGHT) {
            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (animType == ANIM_TOP) {
            activity.overridePendingTransition(R.anim.top_down, R.anim.bottom_down);
        } else if (animType == ANIM_BOTTOM) {
            activity.overridePendingTransition(R.anim.bottom_up, R.anim.top_up);
        } else if (animType == ANIM_FADE) {
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void setAnimFrag(FragmentTransaction tr, int animType) {
        if (animType == ANIM_NO_ANIM) {
            tr.setCustomAnimations(0, 0);
        } else if (animType == ANIM_LEFT) {
            tr.setCustomAnimations(R.anim.left_in, R.anim.right_out, R.anim.right_in, R.anim.left_out);
        } else if (animType == ANIM_RIGHT) {
            tr.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        } else if (animType == ANIM_TOP) {
            tr.setCustomAnimations(R.anim.top_down, R.anim.bottom_down, R.anim.bottom_up, R.anim.top_up);
        } else if (animType == ANIM_BOTTOM) {
            tr.setCustomAnimations(R.anim.bottom_up, R.anim.top_up, R.anim.top_down, R.anim.bottom_down);
        } else if (animType == ANIM_FADE) {
            tr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void finish(Activity activity, int animType) {
        if (animType == ANIM_NO_ANIM) {
            activity.overridePendingTransition(0, 0);
        } else if (animType == ANIM_LEFT) {
            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (animType == ANIM_RIGHT) {
            activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (animType == ANIM_TOP) {
            activity.overridePendingTransition(R.anim.bottom_up, R.anim.top_up);
        } else if (animType == ANIM_BOTTOM) {
            activity.overridePendingTransition(R.anim.top_down, R.anim.bottom_down);
        } else if (animType == ANIM_FADE) {
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public static byte[] objectToByte(Serializable object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static Object bytesToObject(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        Object object = null;
        try {
            in = new ObjectInputStream(bis);
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
