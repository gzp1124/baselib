package com.aligit.base.bindingadapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter


/*
如果 ImageView 对象同时使用了 imageUrl 和 error，
并且 imageUrl 是字符串，error 是 Drawable，就会调用适配器。
如果您希望在设置了任意属性时调用适配器，则可以将适配器的可选 requireAll 标志设置为 false
使用方式
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
 */
@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun loadImage(view: ImageView, url: String?, error: Drawable?) {
//        Picasso.get().load(url).error(error).into(view)
}
