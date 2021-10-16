package com.aligit.base.bindingadapter

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.aligit.base.ext.getImageLoader
import com.aligit.base.ext.tool.dp2px


/*
如果 ImageView 对象同时使用了 imageUrl 和 error，
并且 imageUrl 是字符串，error 是 Drawable，就会调用适配器。
如果您希望在设置了任意属性时调用适配器，则可以将适配器的可选 requireAll 标志设置为 false
使用方式
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
 */
/**
 * 单位使用 dp
 * @param gShowType 0矩形，1圆形，2圆角，3分别设置四个圆角
 */
@BindingAdapter(
    value = ["gImageUrl", "gError", "gLoading", "gShowType", "gRadius",
            "gLeftTop", "gLeftBottom", "gRightTop", "gRightBottom"],
    requireAll = false
)
fun loadImage(
    view: ImageView,
    url: String?,
    @DrawableRes error: Int?,
    @DrawableRes loading: Int?,
    showType: Int = 0,// 0矩形，1圆形，2圆角，3分别设置四个圆角
    radius: Int = 1,
    leftTop: Int = 0,
    leftBottom: Int = 0,
    rightTop: Int = 0,
    rightBottom: Int = 0,
) {
    when (showType) {
        0 -> getImageLoader().loadImage(view.context, url, error, loading, view)
        1 -> getImageLoader().loadCircleImage(view.context, url, error, loading, view)
        2 -> getImageLoader().loadRoundedImage(view.context, url, error, loading, view, dp2px(radius))
        3 -> getImageLoader().load4RoundedImage(view.context, url, error, loading, view, dp2px(leftTop), dp2px(leftBottom), dp2px(rightTop), dp2px(rightBottom))
    }
}
