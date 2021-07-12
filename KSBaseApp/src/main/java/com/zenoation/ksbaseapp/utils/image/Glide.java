package com.zenoation.ksbaseapp.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by kisoojo on 2020.02.03
 */

public class Glide {

    //private static class LazyHolder {
    //    private static final Glide INSTANCE = new Glide();
    //}
    //
    //public static Glide getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static Glide uniqueInstance;

    public static Glide getInstance() {
        if (uniqueInstance == null) {
            synchronized (Glide.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Glide();
                }
            }
        }
        return uniqueInstance;
    }

    private Glide() {}

    private RequestOptions OptionCircle() {
        return RequestOptions.bitmapTransform(new CircleCrop())
                .error(null)
                .placeholder(null);
    }

    private RequestOptions OptionNoPlaceholderCircle() {
        return RequestOptions.bitmapTransform(new CircleCrop())
                .error(null);
    }


    private RequestOptions OptionNoPalce() {
        return RequestOptions.errorOf(null);
    }

    private RequestOptions Option() {
        return RequestOptions.errorOf(null)
                .placeholder(null);
    }

    private RequestOptions Option(@DrawableRes int resId) {
        return RequestOptions.errorOf(null)
                .placeholder(resId);
    }

    private RequestOptions OptionRound() {
        return RequestOptions.bitmapTransform(new RoundedCorners(100))
                .error(null)
                .placeholder(null);
    }

    private RequestOptions OptionRound(@DrawableRes int resId) {
        return RequestOptions.bitmapTransform(new RoundedCorners(100))
                .error(null)
                .placeholder(resId);
    }

    public void load(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).apply(OptionNoPalce()).into(imageView);
    }

    public void loadCircle(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).apply(OptionCircle()).into(imageView);
    }

    public void loadRound(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).apply(OptionRound()).into(imageView);
    }

    public void loadRound(Context context, String strUrl, ImageView imageView, @DrawableRes int resId) {
        com.bumptech.glide.Glide.with(context).load(strUrl).apply(OptionRound(resId)).into(imageView);
    }

    public void loadAnimation(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).transition(withCrossFade(200)).into(imageView);
    }

    public void loadAnimation(Context context, String strUrl, ImageView imageView, @DrawableRes int resId) {
        com.bumptech.glide.Glide.with(context).load(strUrl).transition(withCrossFade(200)).apply(Option(resId)).into(imageView);
    }

    public void loadCircleAnimation(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).transition(withCrossFade(200)).apply(OptionNoPlaceholderCircle()).into(imageView);
    }

    public void loadRoundAnimation(Context context, String strUrl, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(strUrl).transition(withCrossFade(200)).apply(OptionRound()).into(imageView);
    }

    public void loadRoundAnimation(Context context, String strUrl, ImageView imageView, @DrawableRes int resId) {
        com.bumptech.glide.Glide.with(context).load(strUrl).transition(withCrossFade(200)).apply(OptionRound(resId)).into(imageView);
    }

    public void loadRoundNotifi(Context context, String strUrl, NotificationTarget imageView) {
        com.bumptech.glide.Glide.with(context).asBitmap().load(strUrl).into(imageView);
    }


    public void load(Context context, Bitmap bitmap, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(bitmap).apply(OptionNoPalce()).into(imageView);
    }

    public void load(Context context, Uri uri, ImageView imageView) {
        com.bumptech.glide.Glide.with(context).load(uri).apply(OptionNoPalce()).into(imageView);
    }
}
