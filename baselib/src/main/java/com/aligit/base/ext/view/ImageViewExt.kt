package com.aligit.base.ext.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.aligit.base.ext.getImageLoader

/**
 * 加载图片
 */
fun ImageView.loadImg(url:String, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null,isCenterCrop:Boolean = true){
    getImageLoader().loadImage(this.context,url,error,loading,isCenterCrop,this)
}

/**
 * 圆形
 */
fun ImageView.loadCircleImg(url: String, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null,isCenterCrop:Boolean = true){
    getImageLoader().loadCircleImage(this.context,url,error,loading,isCenterCrop,this)
}

/**
 * 圆角
 */
fun ImageView.loadRoundedImg(url: String,radius:Int, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null,isCenterCrop:Boolean = true){
    getImageLoader().loadRoundedImage(this.context,url,error,loading,isCenterCrop,this,radius)
}

/**
 * 分别设置四个角的圆角，单位 dp
 */
fun ImageView.load4RoundedImg(url: String, leftTop:Int, leftBottom:Int, rightTop:Int, rightBottom:Int, @DrawableRes error: Int? = null, @DrawableRes loading: Int? = null,isCenterCrop:Boolean = true){
    getImageLoader().load4RoundedImage(this.context,url,error,loading,isCenterCrop,this,leftTop, leftBottom, rightTop, rightBottom)
}