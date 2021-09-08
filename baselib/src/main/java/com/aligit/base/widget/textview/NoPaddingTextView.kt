package com.aligit.base.widget.textview

import android.content.Context
import android.graphics.Paint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.LineHeightSpan
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.aligit.base.ext.tool.dp2px


/**
 * 去掉Padding的TextView
 */
class NoPaddingTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        includeFontPadding = false
    }
    override fun setText(text: CharSequence?, type: BufferType?) {
        if (text == null) {
            return
        }
        val textHeight = textSize.toInt()+dp2px(7)
        val ssb: SpannableStringBuilder
        if (text is SpannableStringBuilder) {
            ssb = text
            ssb.setSpan(
                ExcludePaddingSpaceSpan(textHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            ssb = SpannableStringBuilder(text)
            ssb.setSpan(
                ExcludePaddingSpaceSpan(textHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        super.setText(ssb, type)
    }
}

class ExcludePaddingSpaceSpan(height: Int) : LineHeightSpan {
    var mHeight = height
    override fun chooseHeight(
        text: CharSequence?,
        start: Int,
        end: Int,
        spanstartv: Int,
        lineHeight: Int,
        fontMetricsInt: Paint.FontMetricsInt
    ) {
        val originHeight = fontMetricsInt.descent - fontMetricsInt.ascent
        if (originHeight <= 0) {
            return
        }
        val ratio = mHeight * 1.0f / originHeight
        fontMetricsInt.descent = Math.round(fontMetricsInt.descent * ratio)
        fontMetricsInt.ascent = fontMetricsInt.descent - mHeight
    }
}