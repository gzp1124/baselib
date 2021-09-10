package com.aligit.base.ext.image

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.luck.picture.lib.engine.ImageEngine

interface ImageLoderEngine : ImageEngine {

    /**
     * @param url
     * @param error 加载出错
     * @param loading 加载中
     */
    fun loadImage(context: Context, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?, imageView: ImageView)
}