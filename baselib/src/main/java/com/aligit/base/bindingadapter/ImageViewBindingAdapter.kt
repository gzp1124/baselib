package com.aligit.base.bindingadapter

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.aligit.base.Settings


/*
如果 ImageView 对象同时使用了 imageUrl 和 error，
并且 imageUrl 是字符串，error 是 Drawable，就会调用适配器。
如果您希望在设置了任意属性时调用适配器，则可以将适配器的可选 requireAll 标志设置为 false
使用方式
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
 */
@BindingAdapter(value = ["gImageUrl", "gError", "gLoading"], requireAll = false)
fun loadImage(view: ImageView, url: String?, @DrawableRes error: Int?, @DrawableRes loading: Int?) {
    url?.let {
        Settings.Tools.imageLoader.loadImage(view.context,it,error,loading,view)
    }
}
