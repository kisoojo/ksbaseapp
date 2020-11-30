package com.zenoation.library.utils.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.zenoation.library.BuildConfig;

import java.util.Set;


/**
 * Created by kisoojo on 2020.02.03
 */

public class PrefUtils {

    public final static String PREF_NAME = "myapp.pref";
    public final static String PREF_FCM_TOKEN = "fcm_token";
    public final static String PREF_UUID = "uuid";
    public final static String PREF_IS_FIRST = "is_first";
    public final static String PREF_FCM_PARAM_TYPE = "fcm_param_type";
    public final static String PREF_FCM_PARAM_IDX = "fcm_param_idx";
    public final static String PREF_FCM_PARAM_RIDX = "fcm_param_ridx";
    public final static String PREF_FCM_PARAM_BDIDX = "fcm_param_bdidx";
    public final static String PREF_FCM_PARAM_MBIDX = "fcm_param_mbidx";
    public final static String PREF_FCM_PARAM_AGENT_MBIDX = "fcm_param_agent_mbidx";
    public final static String PREF_FCM_PARAM_AGENT_CODE = "fcm_param_agent_code";
    public final static String PREF_REQ_INFO = "request_info";
    public final static String PREF_MEMBER_INFO = "member_info";

    public final static String PREF_IDCARD_MSG_NO_SHOW = "id_card_msg_no_show";


    //private static class LazyHolder {
    //    private static final PrefUtils INSTANCE = new PrefUtils();
    //}
    //
    //public static PrefUtils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static PrefUtils uniqueInstance;

    public static PrefUtils getInstance() {
        if (uniqueInstance == null) {
            synchronized (PrefUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PrefUtils();
                }
            }
        }
        return uniqueInstance;
    }

    private PrefUtils() {}

    public void put(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public void put(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public void put(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public void put(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.apply();
    }

    public void put(Context context, String key, Set<String> value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putStringSet(key, value);
        editor.apply();
    }

    public String get(Context context, String key, String dftValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public boolean get(Context context, String key, boolean dftValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public int get(Context context, String key, int dftValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public long get(Context context, String key, long dftValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public Set<String> get(Context context, String key, Set<String> dftValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            return pref.getStringSet(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public void remove(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(key);
        editor.apply();
    }

    public void removeAllPushParams(Context context) {
        remove(context, PREF_FCM_PARAM_TYPE);
        remove(context, PREF_FCM_PARAM_IDX);
        remove(context, PREF_FCM_PARAM_RIDX);
        remove(context, PREF_FCM_PARAM_BDIDX);
        remove(context, PREF_FCM_PARAM_MBIDX);
        remove(context, PREF_FCM_PARAM_AGENT_MBIDX);
        remove(context, PREF_FCM_PARAM_AGENT_CODE);
    }
}
