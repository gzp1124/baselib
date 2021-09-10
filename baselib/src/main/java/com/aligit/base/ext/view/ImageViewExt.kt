package com.aligit.base.ext.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.aligit.base.Settings

/**
 * 加载图片
 */
fun ImageView.loadImg(url:String, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null){
    Settings.Tools.imageLoader.loadImage(this.context,url,error,loading,this)
}

/**
 * 圆形
 */
fun ImageView.loadCircleImg(url: String, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null){
    Settings.Tools.imageLoader.loadCircleImage(this.context,url,error,loading,this)
}

/**
 * 圆角
 */
fun ImageView.loadRoundedImg(url: String,radius:Int, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null){
    Settings.Tools.imageLoader.loadRoundedImage(this.context,url,error,loading,this,radius)
}

/**
 * 分别设置四个角的圆角，单位 dp
 */
fun ImageView.load4RoundedImg(url: String, leftTop:Int, leftBottom:Int, rightTop:Int, rightBottom:Int, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null){
    Settings.Tools.imageLoader.load4RoundedImage(this.context,url,error,loading,this,leftTop, leftBottom, rightTop, rightBottom)
}