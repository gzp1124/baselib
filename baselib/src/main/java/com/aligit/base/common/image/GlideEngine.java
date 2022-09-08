package com.aligit.base.common.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aligit.base.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.utils.ActivityCompatHelper;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class GlideEngine implements ImageLoderEngine {

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, int maxWidth, int maxHeight) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(maxWidth, maxHeight)
                .into(imageView);
    }

    /**
     * 加载相册目录封面
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadAlbumCover(Context context, String url, ImageView imageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .override(180, 180)
                .sizeMultiplier(0.5f)
                .transform(new CenterCrop(), new RoundedCorners(8))
                .placeholder(R.drawable.ps_image_placeholder)
                .into(imageView);
    }


    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(Context context, String url, ImageView imageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .placeholder(R.drawable.ps_image_placeholder)
                .into(imageView);
    }

    @Override
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }


    private GlideEngine() {
    }

    private static GlideEngine instance;

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }

    private void _loadImg(@NotNull Context context, @Nullable String url, @Nullable Integer error, @Nullable Integer loading, boolean isCenterCrop, @NotNull ImageView imageView, Transformation<Bitmap>... transformations) {
        RequestBuilder<Drawable> gb = Glide.with(context).load(url);
        if (error != null) gb.error(error);
        if (loading != null) gb.placeholder(loading);
        gb.transform(transformations);
        gb.into(imageView);
    }

    /**
     * 带加载中和加载失败
     */
    @Override
    public void loadImage(@NotNull Context context, @Nullable String url, @Nullable Integer error, @Nullable Integer loading, boolean isCenterCrop, @NotNull ImageView imageView) {
        if (isCenterCrop) {
            _loadImg(context, url, error, loading, isCenterCrop, imageView, new CenterCrop());
        } else {
            _loadImg(context, url, error, loading, isCenterCrop, imageView);
        }
    }

    /**
     * 圆形
     */
    @Override
    public void loadCircleImage(@NotNull Context context, @Nullable String url, @Nullable Integer error, @Nullable Integer loading, @Nullable boolean isCenterCrop, @NotNull ImageView imageView) {
        _loadImg(context, url, error, loading, isCenterCrop, imageView, new CircleCrop());
    }

    /**
     * 圆角矩形
     */
    @Override
    public void loadRoundedImage(@NotNull Context context, @Nullable String url, @Nullable Integer error, @Nullable Integer loading, @Nullable boolean isCenterCrop, @NotNull ImageView imageView, int radius) {
        if (isCenterCrop) {
            _loadImg(context, url, error, loading, isCenterCrop, imageView, new CenterCrop(), new RoundedCorners(radius));
        } else {
            _loadImg(context, url, error, loading, isCenterCrop, imageView, new RoundedCorners(radius));
        }
    }

    /**
     * 分别设置四个圆角
     */
    @Override
    public void load4RoundedImage(@NotNull Context context, @Nullable String url, @Nullable Integer error, @Nullable Integer loading, @Nullable boolean isCenterCrop, @NotNull ImageView imageView, int leftTop, int leftBottom, int rightTop, int rightBottom) {
        if (isCenterCrop) {
            _loadImg(context, url, error, loading, isCenterCrop, imageView, new CenterCrop(), new GranularRoundedCorners(leftTop, rightTop, rightBottom, leftBottom));
        } else {
            _loadImg(context, url, error, loading, isCenterCrop, imageView, new GranularRoundedCorners(leftTop, rightTop, rightBottom, leftBottom));
        }
    }

    @Override
    public void loadBitmap(@NotNull Context context, @Nullable String url, @Nullable Function1<? super Bitmap, Unit> onBack) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (onBack != null) {
                            onBack.invoke(resource);
                        }
                    }
                });
    }
}
