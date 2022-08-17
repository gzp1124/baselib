package com.aligit.base.widget.round

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author 小垚
 * @创建时间： 2021/4/21
 * @描述：
 **/
class RoundFrameLayout:FrameLayout {
    constructor(context: Context) : super(context){
        delegate = RoundViewDelegate(this,context,null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        delegate = RoundViewDelegate(this,context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        delegate = RoundViewDelegate(this,context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        delegate = RoundViewDelegate(this,context,attrs)
    }

    lateinit var delegate: RoundViewDelegate
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate.isWidthHeightEqual && width > 0 && height > 0) {
            val max = width.coerceAtLeast(height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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