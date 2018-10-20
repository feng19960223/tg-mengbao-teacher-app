package com.turingoal.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.turingoal.android.photopicker.manager.GlideApp;
import com.turingoal.mengbao.teacher.R;

import java.security.MessageDigest;

/**
 * Glide工具类
 */

public class GlideUtil {

    /**
     * 加载圆型图片
     */
    public static void loadImage(final Context context, final String url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .circleCrop() // 圆
                .error(R.drawable.ic_avatar_default) // 错误图片
                .fallback(R.drawable.ic_avatar_default) // 座位图
                .placeholder(R.drawable.ic_avatar_default) // url空图
                .into(imageView);
    }

    /**
     * 普通加载
     */
    public static void load(final Context context, final String url, final ImageView imageView) {
        load(context, url, imageView, R.drawable.ic_iv_error);
    }

    /**
     * 普通加载
     */
    public static void load(final Context context, final String url, final ImageView imageView, int resId) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .error(resId) // 错误图片
                .fallback(resId) // 座位图
                .placeholder(resId) // url空图
                .into(imageView);
    }

    /**
     * 高斯模糊普通加载
     */
    public static void loadBlur(final Context context, final String url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .error(R.drawable.ic_avatar_default) // 错误图片
                .fallback(R.drawable.ic_avatar_default) // 座位图
                .placeholder(R.drawable.ic_avatar_default) // url空图
                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(context))) // 圆形高斯模糊
                .into(imageView);
    }

}

/**
 * 高斯模糊
 */
class GlideBlurformation extends BitmapTransformation {
    private Context context;

    public GlideBlurformation(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = BlurBitmapUtil.instance().blurBitmap(context, toTransform, 25, outWidth, outHeight); // 高斯模糊
        return bitmap;
        // return ImageUtils.toRound(bitmap); // 转为圆形,如果是高斯模糊加圆形，必须要在这里转圆形，在Glide转，没用
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}
