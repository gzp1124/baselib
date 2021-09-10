package com.aligit.base.common.image

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.luck.picture.lib.engine.ImageEngine

interface ImageLoderEngine : ImageEngine {

    /**
     * 设置加载中 加载出错
     * @param url
     * @param error 加载出错
     * @param loading 加载中
     */
    fun loadImage(context: Context, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?, imageView: ImageView)

    /**
     * 圆形
     */
    fun loadCircleImage(context: Context, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?, imageView: ImageView)

    /**
     * 圆角
     */
    fun loadRoundedImage(context: Context, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?, imageView: ImageView, radius:Int)

    /**
     * 分别设置四个角的圆角
     */
    fun load4RoundedImage(context: Context, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?, imageView: ImageView, leftTop:Int, leftBottom:Int, rightTop:Int, rightBottom:Int)
}