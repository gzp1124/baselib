package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import com.aligit.base.ext.dowithTry
import com.xuexiang.xui.utils.ColorUtils
import com.xuexiang.xui.widget.progress.CircleProgressView


/**
 * 设置圆形进度的数据
 * gProgress 当前进度
 * gStartProgress 进度开始，默认0
 * gProgressColor 进度颜色(不渐变)，优先使用 gStartColor 和 gEndColor
 * gStartColor 进度起始颜色(优先)
 * gEndColor 进度结束颜色
 * gTrackColor 进度条的底色（默认取进度颜色的透明 0.1）
 * gDuration 动画执行时间
 */
@BindingAdapter(
    value = ["gProgress", "gStartProgress", "gProgressColor", "gStartColor", "gEndColor", "gTrackColor","gDuration"],
    requireAll = false
)
fun setData(
    view: CircleProgressView,
    gress: Double,
    startProgress: Double? = 0.0,
    progressColor: Int? = null,
    startColor: Int? = null,
    endColor: Int? = null,
    trackColor: Int? = null,
    duration: Int = 1000
) {
    view.run {
        dowithTry{
            (startColor ?: progressColor)?.let { it1 -> setStartColor(it1) }
            (endColor ?: progressColor)?.let { it1 -> setEndColor(it1) }
            trackColor?.let {
                setTrackColor(trackColor)
            } ?: (startColor ?: progressColor)?.let { it1 -> setTrackColor(ColorUtils.setColorAlpha(it1, 0.1f)) }
            setStartProgress((startProgress ?: 0).toFloat())
            setEndProgress(gress.toFloat())
            setProgressDuration(duration)
            startProgressAnimation()
        }
    }
}