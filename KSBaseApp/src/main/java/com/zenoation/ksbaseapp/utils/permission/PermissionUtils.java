package com.zenoation.ksbaseapp.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zenoation.ksbaseapp.listener.OnCompleteListener;

import java.util.ArrayList;

public class PermissionUtils {

    //private static class LazyHolder {
    //    private static final PermissionUtils INSTANCE = new PermissionUtils();
    //}
    //
    //public static PermissionUtils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static PermissionUtils uniqueInstance;

    public static PermissionUtils getInstance() {
        if (uniqueInstance == null) {
            synchronized (PermissionUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PermissionUtils();
                }
            }
        }
        return uniqueInstance;
    }

    private PermissionUtils() {}

    public interface OnPermissionGrantedListener {
        void onPermissionGranted();
    }

    public interface OnPermissionDeniedListener {
        void onPermissionDenied();
    }

    // @RequiresApi(api = Build.VERSION_CODES.R)
    private String[] removePermissionWriteExternalStorage(String[] permissions) {
        ArrayList<String> permissionsArray = new ArrayList();
        for (String p : permissions) {
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(p)) {
                continue;
            }
            permissionsArray.add(p);
        }
        String[] permissionsNew = new String[permissionsArray.size()];
        return permissionsArray.toArray(permissionsNew);
    }

    public void requestPermissions(final Activity activity, String[] permissions,
                                   final OnPermissionGrantedListener permissionGrantedListener, final boolean bIsBack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permissions = removePermissionWriteExternalStorage(permissions);
        }
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (permissionGrantedListener != null) {
                    permissionGrantedListener.onPermissionGranted();
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                try {
                    Toast.makeText(activity, "[설정] > [권한] 에서 권한을 허용을 하셔야 합니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + activity.getApplication().getPackageName()));
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, "[설정] > [권한] 에서 권한을 허용을 하셔야 합니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                            .setData(Uri.parse("package:" + activity.getApplication().getPackageName()));
                    activity.startActivity(intent);
                }
                if (bIsBack) {
                    activity.onBackPressed();
                }
            }
        };
        TedPermission.with(activity)
                .setPermissionListener(permissionListener)
                .setPermissions(permissions)
                .check();
    }

    public boolean isPermissionGranted(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permissions = removePermissionWriteExternalStorage(permissions);
        }
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions(final Activity activity, String[] permissions,
                                   final OnPermissionGrantedListener permissionGrantedListener,
                                   final OnPermissionDeniedListener permissionDeniedListener) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permissions = removePermissionWriteExternalStorage(permissions);
        }

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (permissionGrantedListener != null) {
                    permissionGrantedListener.onPermissionGranted();
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                if (permissionDeniedListener != null) {
                    permissionDeniedListener.onPermissionDenied();
                }
            }
        };

        // String strPermission = null;
        // for (int i = 0; i < permissions.length; i++) {
        //     if (i == 0) {
        //         strPermission = permissions[i];
        //     } else {
        //         strPermission += ", ";
        //     }
        // }

        TedPermission.with(activity)
                .setPermissionListener(permissionListener)
                .setPermissions(permissions)
                .check();
    }

    // @RequiresApi(api = Build.VERSION_CODES.R)
    // public void requestPermissionExternalStorage(Activity activity, OnCompleteListener listener) {
    //     if (Environment.isExternalStorageManager()) {
    //         if (listener != null) {
    //             listener.onComplete();
    //         }
    //     } else {
    //         Toast.makeText(activity, "모든 파일 관리 권한을 허용을 하셔야 합니다.", Toast.LENGTH_LONG).show();
    //         Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
    //                 .setData(Uri.parse("package:" + activity.getApplication().getPackageName()));
    //         activity.startActivity(intent);
    //     }
    // }
}
