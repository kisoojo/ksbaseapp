package com.zenoation.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gun0912.tedpermission.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.zenoation.library.base.BaseApplication.TEMP_PATH;


/**
 * Created by kisoojo on 2020.02.03
 */
public class Utils {

    private final static String TAG = "Utils"; 
    
    private Toast mToast;

    private Utils() {
    }

    //private static class LazyHolder {
    //    private static final Utils INSTANCE = new Utils();
    //}
    //
    //public static Utils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static Utils uniqueInstance;

    public static Utils getInstance() {
        if (uniqueInstance == null) {
            synchronized (Utils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Utils();
                }
            }
        }
        return uniqueInstance;
    }

    /**
     * 키해시 구하기
     *
     * @param context
     * @return
     */
    public String getKeyHash(Context context) {
        String keyhash = "";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature sIgnature : pi.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(sIgnature.toByteArray());
                keyhash = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, "Key Hash : " + keyhash);
                // DialogUtils.getInstance().showAlertDialog((Activity) context, keyhash);
                // Log.i("OnvitPlatform", "Key Hash : " + keyhash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return keyhash;
    }


    /**
     * 디버그모드 여부 반환
     *
     * @param context
     * @return boolean
     */
    public boolean isDebugMod(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return debuggable;
    }

    /**
     * sleep
     */
    public void sleep(int millsec) {
        try {
            Thread.sleep(millsec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 토스트 출력
     */

    public void showToast(Context context, String msg) {
        showToast(context, msg, false);
    }

    public void showToast(Context context, String msg, boolean bIsUpdate) {
        try {
            if (context != null && !TextUtils.isEmpty(msg)) {
                if (mToast == null || !bIsUpdate)
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                else
                    mToast.setText(msg);
//		    mToast.setGravity(Gravity.BOTTOM, 0, GetPxFromDp(context, 100));
                mToast.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void showToastCenter(Context context, String msg) {
        try {
            if (context != null && !TextUtils.isEmpty(msg)) {
                if (mToast == null)
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                else
                    mToast.setText(msg);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void showToastTop(Context context, String msg) {
        try {
            if (context != null && !TextUtils.isEmpty(msg)) {
                if (mToast == null)
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                else
                    mToast.setText(msg);
                mToast.setGravity(Gravity.TOP, 0, getPxFromDp(context, 100));
                mToast.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 타이틀바 숨기기
     */
    public void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    /**
     * 스테이터스바 숨기기
     */
    public void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 소프트 키패드 보이기
     *
     * @param context
     * @param view
     */
    public void showSoftInput(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 소프트 키패드 숨기기
     *
     * @param context
     * @param view
     */
    public void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 단말기 density 구함
     *
     * @param con 사용법 : if(getDensity(context) == 2f && (float으로 형변환해서 사용 해야함.)
     */
    public float getDensity(Context con) {
        return con.getResources().getDisplayMetrics().density;
    }

//	/**
//	 * px을 dp로 변환
//	 * @param con
//	 * @param px
//	 * @return dp
//	 */
//	public int GetPxToDp(Context con, int px) {
//	    float density = 0.0f;
//	    density  = con.getResources().getDisplayMetrics().density;
//	    return (int)(px / density);
//	}
//
//	/**
//	 * dp를 px로 변환
//	 * @param con
//	 * @param dp
//	 * @return px
//	 */
//	public int GetDpToPx(Context con, double dp) {
//	    float density = 0.0f;
//	    density  = con.getResources().getDisplayMetrics().density;
//	    return (int)(dp * density + 0.5);
//	}

    /**
     * px을 dp로 변환
     */
    public int getDpFromPx(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / displayMetrics.density);
    }

    /**
     * dp를 px로 변환
     */
    public int getPxFromDp(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }


    /**
     * 표시할 수 있는 화면 가로크기
     */
    public int getDisplayWidth(Activity activity) {
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        return displayRectangle.width();
    }

    /**
     * 표시할 수 있는 화면 세로크기
     */
    public int getDisplayHeight(Activity activity) {
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        return displayRectangle.height();
    }

    /**
     * 단말기 가로 해상도 구하기
     *
     * @return width
     */
    public int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 단말기 세로 해상도 구하기
     *
     * @return hight
     */
    public int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 네비게이션바 유무 확인
     *
     * @return
     */
    public boolean hasNavigationBar() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        return (!(hasBackKey && hasHomeKey));
    }

    public Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    /**
     * 버튼 클릭 방지
     *
     * @param delay - 기본 지연 0.5초(500 milliseconds)
     */
    public void setPreventButtonClick(final View v, int delay) {
        v.setClickable(false);
        new Handler().postDelayed(() -> {
            try {
                v.setClickable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, (delay == 0) ? 500 : delay);
    }

    /*********************************************************************************************************************************
     *
     */

    /**
     * 비트맵 사이즈
     */
    public int[] getBitmapSize(String filePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (Exception e) {
            return new int[]{0, 0};
        }
    }

    /**
     * 비트맵 width
     */
    public int getBitmapOfWidth(String filePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            return options.outWidth;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 비트맵 height
     */
    public int getBitmapOfHeight(String filePath) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            return options.outHeight;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 비트맵 drawable 변환
     */
    public Drawable getDrawableFromBitmap(Context context, Bitmap bm) {
        return new BitmapDrawable(context.getResources(), bm);
    }

    /**
     * drawable 비트맵 변환
     */
    public Bitmap getBitmapFromDrawable(Drawable d) {
        BitmapDrawable bd = (BitmapDrawable) d;
        return bd.getBitmap();
    }

    /**
     * 비트맵 겹치기
     */
    public Bitmap overlayMark(Bitmap bm1, Bitmap bm2, int distanceLeft, int distanceTop) {
        Bitmap bmOverlay = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), bm1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bm1, 0, 0, null);
        canvas.drawBitmap(bm2, distanceLeft, distanceTop, null);
        return bmOverlay;
    }

    /**
     * 비트맵 라운딩 효과
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPixel, roundPixel, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    /**
     * 비트맵 라운딩 효과
     */
    public Bitmap getRoundedCornerBorderBitmap(Bitmap bitmap, int borderColor, float roundPixel, int borderWidth) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);

        //--CROP THE IMAGE
        canvas.drawRoundRect(rectF, roundPixel, roundPixel, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //--ADD BORDER IF NEEDED
        if (roundPixel > 0) {
            int roundPixel2 = (int) Math.ceil(roundPixel / borderWidth);
            final Paint paint2 = new Paint();
            final Rect rect2 = new Rect(rect.left + roundPixel2, rect.top + roundPixel2, rect.right - roundPixel2, rect.bottom - roundPixel2);
            final RectF rectF2 = new RectF(rect2);
            paint2.setAntiAlias(true);
            paint2.setColor(borderColor);
            paint2.setStrokeWidth(borderWidth);
            paint2.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(rectF2, roundPixel, roundPixel, paint2);
        }

        return output;
    }

    /**
     * 비트맵 원형 효과
     */
    public Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int size = (bitmap.getWidth() / 2);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(size, size, size, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 비트맵 원형 효과 + 색깔 테두리
     */
    public Bitmap getCircleBorderBitmap(Bitmap bitmap, int borderColor, int borderWidth) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        //--CROP THE IMAGE
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - 1, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //--ADD BORDER IF NEEDED
        if (borderWidth > 0) {
            final Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setColor(borderColor);
            paint2.setStrokeWidth(borderWidth);
            paint2.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, (float) (bitmap.getWidth() / 2 - Math.ceil(borderWidth / 2)), paint2);
        }

        return output;
    }

    /**
     * 비트맵 그림자 효과
     */
    public Bitmap getShadowBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float blurSize;
        if (width > height)
            blurSize = (float) (height * 0.02);
        else
            blurSize = (float) (width * 0.02);

        BlurMaskFilter filter1 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL);
        BlurMaskFilter filter2 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.INNER);
        BlurMaskFilter filter3 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.OUTER);
        BlurMaskFilter filter4 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.SOLID);

        EmbossMaskFilter filter5 = new EmbossMaskFilter(new float[]{2, 2, 2}, 0.5f, 5, 1);
        EmbossMaskFilter filter6 = new EmbossMaskFilter(new float[]{2, 2, 2}, 0.5f, 5, 3);
        EmbossMaskFilter filter7 = new EmbossMaskFilter(new float[]{2, 2, 2}, 0.5f, 5, 6);
        EmbossMaskFilter filter8 = new EmbossMaskFilter(new float[]{2, 2, 2}, 0.5f, 5, 10);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final int[] offsetXY = new int[2];

        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter4);

        Bitmap blurBitmap = bitmap.extractAlpha(paint, offsetXY);
        Bitmap output = Bitmap.createBitmap(blurBitmap.getWidth() + Math.round(blurSize), blurBitmap.getHeight() + Math.round(blurSize), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        canvas.drawBitmap(blurBitmap, 0, 0, paint);
        canvas.drawBitmap(bitmap, -offsetXY[0] - blurSize, -offsetXY[1] - blurSize, paint);

        return output;
    }

    /**
     * 비트맵 블러 효과
     */
    public Bitmap getBlurBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float blurSize;
        if (width > height)
            blurSize = (float) (height * 0.02);
        else
            blurSize = (float) (width * 0.02);

        BlurMaskFilter filter1 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL);
        BlurMaskFilter filter2 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.INNER);
        BlurMaskFilter filter3 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.OUTER);
        BlurMaskFilter filter4 = new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.SOLID);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final int[] offsetXY = new int[2];

        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter1);

        //Bitmap blurBitmap = bitmap.extractAlpha(paint, offsetXY);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    /**
     * 비트맵 리사이즈
     */
    public Bitmap resizeBitmap(String path, int maxWidthAndHeight) {
        //이미지 샘플링
        BitmapFactory.Options option = new BitmapFactory.Options();

        int preWidth = getBitmapOfWidth(path);
        int preHeight = getBitmapOfHeight(path);

        int sampleW = (preWidth <= maxWidthAndHeight) ? 1 : Math.round(preWidth / maxWidthAndHeight);
        int sampleH = (preHeight <= maxWidthAndHeight) ? 1 : Math.round(preHeight / maxWidthAndHeight);
        option.inSampleSize = (sampleW > sampleH) ? sampleW : sampleH;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            option.inPurgeable = true;
        }
        option.inDither = true;
        option.inJustDecodeBounds = false;
        option.inPreferQualityOverSpeed = true;
        option.inPreferredConfig = Config.ARGB_8888;

        Bitmap photo = BitmapFactory.decodeFile(path, option);

        int imgWidth = photo.getWidth();
        int imgHeight = photo.getHeight();
        float ratio;
        if (imgWidth > imgHeight)
            ratio = (float) imgWidth / maxWidthAndHeight;
        else
            ratio = (float) imgHeight / maxWidthAndHeight;

        photo = Bitmap.createScaledBitmap(photo, (ratio <= 1.0f) ? imgWidth : (int) (imgWidth / ratio), (ratio <= 1.0) ? imgHeight : (int) (imgHeight / ratio), true);

        return photo;
    }

    /**
     * View 내용을 Bitmap 으로 변환
     *
     * @param view
     * @return
     */
    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        bitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmap);
        if (view instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) view;
            surfaceView.setBackgroundColor(Color.TRANSPARENT);
            surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
            surfaceView.setZOrderOnTop(true);
            surfaceView.draw(canvas);
            surfaceView.setZOrderOnTop(false);
            return bitmap;
        } else {
            //For ViewGroup & View
            view.draw(canvas);
            return bitmap;
        }
    }


    /**
     * Fade in 효과
     */
    public void fadeInAnimate(View imageView, int durationMillis) {
        if (imageView != null) {
            AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
            fadeImage.setDuration(durationMillis);
            fadeImage.setInterpolator(new DecelerateInterpolator());
            imageView.startAnimation(fadeImage);
        }
    }


    /**
     * Bitmap => File
     */
    public String saveFileFromBitmap(Bitmap bitmap, String name) {
        String fileName = TEMP_PATH + File.separator + name;
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            OutputStream os;
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }



    /*********************************************************************************************************************************
     *
     */


    /**
     * List 의 현재 Item view 가져오기
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    /*********************************************************************************************************************************
     *
     */


    /**
     * Input filter - 영문만 허용
     */
    public InputFilter filterAlpha = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };


    /**
     * Input filter - 숫자만 허용
     */
    public InputFilter filterNumeric = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };


    /**
     * Input filter - 영문/숫자 허용
     */
    public InputFilter filterAlphaNumeric = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };


    /**
     * Input filter - 영문/숫자/한글 허용
     */
    public InputFilter filterAlphaNumericHangul = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\u318D\u119E\u11A2\u2022\u2025a\u00B7\uFE55]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };


    /**
     * Input filter - 대문자
     */
    public InputFilter filterUpperCase = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return source.toString().toUpperCase();
        }
    };


    /**
     * Input filter - 소문자
     */
    public InputFilter filterLowerCase = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return source.toString().toLowerCase();
        }
    };


    /**
     * Input filter - 이메일
     */
    public InputFilter filterEmail = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9\\@\\.]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };


    /**
     * Input filter - 16진수
     */
    public InputFilter filterHex = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-fA-F0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }

            return null;
        }
    };

    /**
     * 이메일 체크
     */
    public boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /*********************************************************************************************************************************
     *
     */


    /**
     * 숫자만 가져오기
     */
    public String getNumberOnly(String str) {
        return str.replaceAll("[^0-9]", "");

        // 또는
        // return str.replaceAll("[^\\d]", "");

        // 또는
        // return str.replaceAll("\\D", "");

        // 모두 사용가능하다.
    }

    /**
     * 전화번호 포맷 변경
     */
    public String getHpFormat(String hpNo) {
        String regEx = "([0-9]{3})([0-9]{3,4})([0-9]{4})";

        if (Pattern.matches(regEx, hpNo))
            return hpNo.replaceAll(regEx, "$1-$2-$3");
        else
            return hpNo;
    }

    /**
     * 전화번호 포맷 설정
     */
    public String getHpFormatRT(String hpNo) {
        String regEx1 = "([0-9]{3})";
        String regEx2 = "([0-9]{3})-([0-9]{3,4})";
        String regEx3 = "([0-9]{3})-([0-9]{3,4})-([0-9]{4})";
        String regEx4 = "([0-9]{3})-([0-9]{3})-([0-9])([0-9]{4})";

        if (Pattern.matches(regEx1, hpNo))
            return hpNo.replaceAll(regEx1, "$1-");
        else if (Pattern.matches(regEx2, hpNo))
            return hpNo.replaceAll(regEx2, "$1-$2-");
        else if (Pattern.matches(regEx3, hpNo))
            return hpNo.replaceAll(regEx3, "$1-$2-$3");
        if (Pattern.matches(regEx4, hpNo))
            return hpNo.replaceAll(regEx4, "$1-$2$3-$4");
        else
            return hpNo;
    }

    /**
     * 휴대전화번호 유효성 체크
     */
    public boolean checkHpValidity(EditText et) {
        boolean bRet = true;
        String phoneNo = et.getText().toString();
        String regEx = "([0-9]{3})-([0-9]{3,4})-([0-9]{4})";

        et.setText(getHpFormat(phoneNo));
        phoneNo = et.getText().toString();

        if (!Pattern.matches(regEx, phoneNo)) {
            bRet = false;
        } else {
            if (!phoneNo.substring(0, 3).equals("010") && !phoneNo.substring(0, 3).equals("011"))
                bRet = false;
        }

        return bRet;
    }

    /**
     * 휴대전화번호 유효성 체크
     */
    public boolean checkHpValidity(String hpNo) {
        boolean bRet = true;
        String regEx = "([0-9]{3})-([0-9]{3,4})-([0-9]{4})";
        String hp = getHpFormat(hpNo);

        if (!Pattern.matches(regEx, hp)) {
            bRet = false;
        } else {
            if (!hp.substring(0, 3).equals("010") && !hpNo.substring(0, 3).equals("011"))
                bRet = false;
        }

        return bRet;
    }

    /**
     * 주민번호 유효성 체크
     */
    public boolean checkJumin(String jumin) {
        boolean isKorean = true;
        int check = 0;

        if (jumin == null || jumin.length() != 13) {
            return false;
        }

        if (Character.getNumericValue(jumin.charAt(6)) > 4 && Character.getNumericValue(jumin.charAt(6)) < 9) {
            isKorean = false;
        }

        for (int i = 0; i < 12; i++) {
            check += ((i % 8 + 2) * Character.getNumericValue(jumin.charAt(i)));
        }

        if (isKorean) {
            check = (11 - (check % 11)) % 10;
        } else {
            check = (13 - (check % 11)) % 10;
        }
        return check == Character.getNumericValue(jumin.charAt(12));
    }

    /**
     * 날짜시간 문자열 반환
     */
    public String getDatetimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return df.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 날짜시간 문자열 반환
     */
    public String getDatetimeString2() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        return df.format(new Date(System.currentTimeMillis()));
    }

    /**
     * UNIXTIME_STAMP 를 날짜시간 문자열로 반환
     *
     * @param timeStamp
     * @return
     */
    public String getDateTimeFromUnixTimeStamp(long timeStamp) {
        Date date = new Date(timeStamp * 1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
/*
        date = new Date(timeStamp); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-9")); // give a timezone reference for formating (see profile_pen at the bottom
        String dateStr = sdf.format(date);
*/
        return String.format(Locale.KOREA, "%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, min, sec);
    }

    /**
     * 날짜형식 변환
     */
    public String getDateTimeString(String date, int format) {
        date = date.substring(0, 19);
        String[] d = date.split(" ");
        String[] ymd = d[0].split("-");
        String[] his = d[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(ymd[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(ymd[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ymd[2]));

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        String dowStr = "";
        if (dow == 1) {
            dowStr = "일";
        } else if (dow == 2) {
            dowStr = "월";
        } else if (dow == 3) {
            dowStr = "화";
        } else if (dow == 4) {
            dowStr = "수";
        } else if (dow == 5) {
            dowStr = "목";
        } else if (dow == 6) {
            dowStr = "금";
        } else if (dow == 7) {
            dowStr = "토";
        }

        String dateStr = "";
        if (format == 1) {
            dateStr = String.format(Locale.KOREA, "%s년 %s월 %s일 (%s)", ymd[0], ymd[1], ymd[2], dowStr);
        } else if (format == 2) {
            dateStr = String.format(Locale.KOREA, "%s년 %s월 %s일", ymd[0], ymd[1], ymd[2]);
        } else if (format == 3) {
            dateStr = String.format(Locale.KOREA, "%s년 %s월 %s일 %s시 %s분 %s초", ymd[0], ymd[1], ymd[2], his[0], his[1], his[2]);
        } else if (format == 4) {
            dateStr = String.format(Locale.KOREA, "%s-%s-%s", ymd[0], ymd[1], ymd[2]);
        } else if (format == 5) {
            dateStr = String.format(Locale.KOREA, "%s.%s.%s", ymd[0], ymd[1], ymd[2]);
        } else if (format == 13) {
            dateStr = String.format(Locale.KOREA, "%s년 %s월 %s일 %s:%s:%s", ymd[0], ymd[1], ymd[2], his[0], his[1], his[2]);
        } else if (format == 14) {
            dateStr = String.format(Locale.KOREA, "%s-%s-%s %s:%s:%s", ymd[0], ymd[1], ymd[2], his[0], his[1], his[2]);
        } else if (format == 142) {
            dateStr = String.format(Locale.KOREA, "%s-%s-%s %s:%s", ymd[0], ymd[1], ymd[2], his[0], his[1]);
        } else if (format == 15) {
            dateStr = String.format(Locale.KOREA, "%s.%s.%s %s:%s:%s", ymd[0], ymd[1], ymd[2], his[0], his[1], his[2]);
        }

        return dateStr;
    }

    /**
     * 오늘인지 판단
     */
    public boolean isToday(String dateTimeStr) {
        String[] dateTime;

        if (TextUtils.isEmpty(dateTimeStr) || dateTimeStr.length() < 19)
            return false;

        dateTime = dateTimeStr.substring(0, 19).split(" ");

        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");

        Calendar today = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return todayYear == year && todayMonth == month && todayDay == day;
    }

    /**
     * 오늘 날짜 가져오기
     */
    public String getTodayDate() {
        Calendar today = Calendar.getInstance();

        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH) + 1;
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        return String.format(Locale.KOREA, "%4d-%02d-%02d", todayYear, todayMonth, todayDay);
    }

    /**
     * 오늘 날짜 숫자로 가져오기
     */
    public int getTodayDateInt() {
        String date = getTodayDate().replace("-", "");
        return Integer.parseInt(date);
    }

    /**
     * 날짜 숫자로 가져오기
     */
    public int getDateInt(String d) {
        String date = d.substring(0, 10).replace("-", "");
        return Integer.parseInt(date);
    }

    /**
     * 날짜 비교
     *
     * @param dateTimeStr ( 0이하 : 미래 / 0이상 : 과거 )
     */
    public long compareDate(String dateTimeStr) {
        String[] dateTime;

        if (TextUtils.isEmpty(dateTimeStr) || dateTimeStr.length() < 19)
            return -1;

        dateTime = dateTimeStr.substring(0, 19).split(" ");

        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");

        Calendar today = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        return today.getTimeInMillis() - cal.getTimeInMillis();
    }

    /**
     * 해당월 최대일자 가져오기
     */
    public int getMaxDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 생년월일 포멧
     */
    public String getBirthFormat(String birth) {
        String birthStr = "";
        if (!TextUtils.isEmpty(birth)) {
            birthStr = String.format(Locale.KOREA, "%s.%s.%s",
                    birth.substring(0, 4),
                    birth.substring(4, 6),
                    birth.substring(6, 8));
        }

        return birthStr;
    }

    /**
     * 생년으로 현재 나이 계산
     */
    public int getAgeFromBirthYear(int birthYear, boolean bIsInternationalAge) {
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int age;

        if (bIsInternationalAge) //만
            age = todayYear - birthYear;
        else //한국 나이
            age = todayYear - birthYear + 1;

        return age;
    }

    /**
     * 현재나이로 생년 계산
     */
    public int getBirthYearFromAge(int age, boolean bIsInternationalAge) {
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int birthYear;

        if (bIsInternationalAge) //만
            birthYear = todayYear - age;
        else //한국 나이
            birthYear = todayYear - age + 1;

        return birthYear;
    }

    /**
     * 화폐 3자리 단위로 점찍기
     */
    public String convertCurrency(int currency) {
        DecimalFormat df = new DecimalFormat("###,###");

        return df.format(currency);
    }

    /**
     * 화폐 3자리 단위로 점찍기
     */
    public String convertCurrency(String currency) {
        DecimalFormat df = new DecimalFormat("###,###");
        String retStr = "";
        try {
            retStr = df.format(Integer.parseInt(currency));
        } catch (NumberFormatException e) {
            retStr = "--";
        }

        return retStr;
    }

    /**
     * 거리 m / km 로 변환
     */
    public String convertDistance(int distance) {
        String del = "m";
        if (distance >= 1000) {
            distance /= 1000;
            del = "Km";
        }

        return convertCurrency(distance) + del;
    }

    /**
     * 텍스트 원하는 줄까지 가져오기
     */
    public String getTextUntilLine(String str, int line) {
        String text = "";
        String[] lines = str.split("\n");
        if (lines != null && lines.length > line) {
            for (int i = 0; i < line; i++) {
                if (i == 0)
                    text = lines[i];
                else
                    text += "\n" + lines[i];
            }
        } else {
            text = str;
        }

        return text;
    }

    /**
     * 문자열을 구분자로 구분해서 ArrayList 로 반환
     */
    public ArrayList<String> getArrayListFromStr(String src, String del) {
        if (src.contains(del)) {
            String[] list = src.split(del);
            return new ArrayList<>(Arrays.asList(list));
        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(src);
            return arrayList;
        }
    }

    /**
     * ArrayList to Array
     */
    public String[] getArrayFromArrayList(ArrayList<String> arrayList) {
        return arrayList.toArray(new String[arrayList.size()]);
    }

    /**
     * Array to ArrayList
     */
    public ArrayList<String> getArrayListFromArray(String[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public void deleteFiles(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFiles(f);
                }
            } else {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(File file) {
        try {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFiles(f);
                }
            } else {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*********************************************************************************************************************************
     *
     */

    /**
     * 기기 전화번호 가져오기
     */
    public String getPhoneNumber(Context context, boolean useHyphen) {
        TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneNumber = systemService.getLine1Number();

        if (phoneNumber == null) {
            phoneNumber = "00000000000";
        } else {
            phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
            phoneNumber = "0" + phoneNumber;
        }

        if (useHyphen)
            return getHpFormat(phoneNumber);//PhoneNumberUtils.formatNumber(phoneNumber);
        else
            return phoneNumber;
    }

    /**
     * 기기 정보 가져오기
     */
    public String getDeviceInfo() {
        return "A(" + Build.VERSION.SDK_INT + ")/" +
                Build.BRAND + "(" + Build.MANUFACTURER + ")" +
                Build.MODEL + "(" + Build.PRODUCT + ")" +
                Build.DEVICE + "/" +
                Build.VERSION.RELEASE + "/" +
                BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")";
    }

    /**
     * SD카드가 마운트 되어 있는지 확인
     */
    public boolean isSDCardMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 모바일 네트워크 On/Off
     */
    public void networkOnOff(Context context, boolean isOn) {
        try {
            ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(conManager, isOn);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 어플 실행중인지 확인
     */
    public boolean isPackageRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, list.get(i).processName);
            if (list.get(i).processName.equals(context.getPackageName()))
                return true;
        }

        return false;
    }

    /**
     * 앱 종료
     */
    public void exitProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void exitProcess(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * package name 가져오기
     */
    public String getPackageName(Context context) {
        try {
            return context.getPackageName();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * package name 가져오기
     */
    public String getPackageNameSimple(Context context) {
        try {
            String pkgName = context.getPackageName();
            String[] pkg = pkgName.split("\\.");
            pkgName = pkg[pkg.length - 1];
            return pkgName;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * version name 가져오기
     */
    public String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * version code 가져오기
     */
    public String getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return Long.toString(pi.getLongVersionCode());
            } else {
                return String.valueOf(pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /*********************************************************************************************************************************
     *
     */

    /**
     * 투명도 조절을 위한 100 : 255
     */
    public String convertAlphaDecToHex(int dec) {
        String hexStr = "";

        if (dec == 0) hexStr = "00";
        else if (dec > 0 && dec <= 5) hexStr = "0D";
        else if (dec > 5 && dec <= 10) hexStr = "1A";
        else if (dec > 10 && dec <= 15) hexStr = "26";
        else if (dec > 15 && dec <= 20) hexStr = "33";
        else if (dec > 20 && dec <= 25) hexStr = "40";
        else if (dec > 25 && dec <= 30) hexStr = "4D";
        else if (dec > 30 && dec <= 35) hexStr = "59";
        else if (dec > 35 && dec <= 40) hexStr = "66";
        else if (dec > 40 && dec <= 45) hexStr = "73";
        else if (dec > 45 && dec <= 50) hexStr = "80";
        else if (dec > 50 && dec <= 55) hexStr = "8C";
        else if (dec > 55 && dec <= 60) hexStr = "99";
        else if (dec > 60 && dec <= 65) hexStr = "A6";
        else if (dec > 65 && dec <= 70) hexStr = "B3";
        else if (dec > 70 && dec <= 75) hexStr = "BF";
        else if (dec > 75 && dec <= 80) hexStr = "CC";
        else if (dec > 80 && dec <= 85) hexStr = "D9";
        else if (dec > 85 && dec <= 90) hexStr = "E6";
        else if (dec > 90 && dec <= 95) hexStr = "F2";
        else if (dec > 95 && dec <= 100) hexStr = "FF";

        return hexStr;
    }


    /**
     * URL 가져오기
     */
    public ArrayList<String> getUrls(String str) {
        Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        Matcher matcher = urlPattern.matcher(str);
        ArrayList<String> ret = new ArrayList<>();
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            ret.add(str.substring(matchStart, matchEnd));
        }
        return ret;
    }

    /**
     * 앱 재실행
     */
    public void restartApplication(Context context, Class activity) {
        try {
            Intent intent = new Intent(context, activity);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            exitProcess();
        }
    }


    /**
     * 에뮬레이터 체크
     */
    public boolean isRunOnEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }


    /**
     * 크롬실행
     */
    public void startChrome(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                //if Chrome browser not installed
                intent.setPackage(null);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
