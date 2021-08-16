package com.aligit.base.widget.round

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/9/11
 * @description : 用于需要圆角矩形框背景的ConstraintLayout的情况,减少直接使用ConstraintLayout时引入的shape资源文件
 * </pre>
 */
class RoundConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var delegate: com.aligit.base.widget.round.RoundViewDelegate =
        com.aligit.base.widget.round.RoundViewDelegate(this, context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate.isWidthHeightEqual && width > 0 && height > 0) {
            val max = width.coerceAtLeast(height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    fun getDelegate(): com.aligit.base.widget.round.RoundViewDelegate {
        return delegate
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (delegate.isRadiusHalfHeight) {
            delegate.cornerRadius = height / 2
        } else {
            delegate.setBgSelector()
        }
    }
}