package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import com.aligit.base.ext.dowithTry
import com.xuexiang.xui.utils.ColorUtils
import com.xuexiang.xui.widget.progress.CircleProgressView
import com.xuexiang.xui.widget.progress.HorizontalProgressView


/**
 * 设置圆形进度的数据
 * gProgress 当前进度
 * gStartProgress 进度开始，默认0
 * gProgressColor 进度颜色(不渐变)，优先使用 gStartColor 和 gEndColor
 * gStartColor 进度起始颜色(优先)
 * gEndColor 进度结束颜色
 * gTrackColor 进度条的底色（默认取进度颜色的透明 0.1）
 * gDuration 动画执行时间 单位 毫秒，默认 1000
 */
@BindingAdapter(
    value = ["gProgress", "gStartProgress", "gProgressColor", "gStartColor", "gEndColor", "gTrackColor","gDuration"],
    requireAll = false
)
fun setCircleData(
    view: CircleProgressView,
    gress: Double,
    startProgress: Double? = 0.0,
    progressColor: Int? = null,
    startColor: Int? = null,
    endColor: Int? = null,
    trackColor: Int? = null,
    duration: Int? = null
) {
    view.run {
        dowithTry{
            setProgressDuration(duration ?: 1000)
            (startColor ?: progressColor)?.let { it1 -> setStartColor(it1) }
            (endColor ?: progressColor)?.let { it1 -> setEndColor(it1) }
            trackColor?.let {
                setTrackColor(trackColor)
            } ?: (startColor ?: progressColor)?.let { it1 -> setTrackColor(ColorUtils.setColorAlpha(it1, 0.1f)) }
            setStartProgress((startProgress ?: 0).toFloat())
            setEndProgress(gress.toFloat())
            startProgressAnimation()
        }
    }
}

@BindingAdapter(
    value = ["gProgress", "gStartProgress", "gProgressColor", "gStartColor", "gEndColor", "gTrackColor","gDuration"],
    requireAll = false
)
fun setHorizontalData(
    view: HorizontalProgressView,
    gress: Double,
    startProgress: Double? = 0.0,
    progressColor: Int? = null,
    startColor: Int? = null,
    endColor: Int? = null,
    trackColor: Int? = null,
    duration: Int? = null
) {
    view.run {
        dowithTry{
            setTrackEnabled(true) //是否显示轨迹背景
            setProgressDuration(duration ?: 1000) // 动画持续时间
            // 进度颜色
            (startColor ?: progressColor)?.let { it1 -> setStartColor(it1) }
            (endColor ?: progressColor)?.let { it1 -> setEndColor(it1) }
            // 底色
            trackColor?.let {
                setTrackColor(trackColor)
            } ?: (startColor ?: progressColor)?.let { it1 -> setTrackColor(ColorUtils.setColorAlpha(it1, 0.1f)) }
            // 设置进度
            setStartProgress((startProgress ?: 0).toFloat())
            setEndProgress(gress.toFloat())
            // 启动动画
            startProgressAnimation()
        }
    }
}