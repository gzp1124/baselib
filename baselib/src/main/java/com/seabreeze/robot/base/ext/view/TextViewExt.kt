package com.seabreeze.robot.base.ext.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.seabreeze.robot.base.ext.tool.sp2px

/**
 * @param text 文字内容
 * @param textSize 大小 sp
 * @param padding 左右间距 dp
 * @param underline 下划线
 * @param typeface 字体
 * @param textColor 颜色
 * @param click 点击事件
 */
data class CustomText(
    var text: String,
    var textSize: Float,
    var padding: Int = 0,
    var underline: Boolean = false,
    var typeface: Typeface = Typeface.DEFAULT,
    @ColorInt var textColor: Int = Color.parseColor("#000000"),
    var click: () -> Unit = {}
)

fun TextView.setCustomText(text: CustomText?) {
    if (text == null) return
    setCustomText(arrayListOf(text))
}

fun TextView.setCustomText(texts: List<CustomText>?) {
    if (texts == null) return
    this.movementMethod = LinkMovementMethod.getInstance()
    val con = SpannableStringBuilder()
    texts.forEach {
        if (it.text.isNotEmpty()) {
            val s = SpannableString(it.text)
            s.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        it.click()
                    }

                    @SuppressLint("ResourceAsColor")
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = it.textColor
                        ds.isUnderlineText = it.underline
                        ds.textSize = sp2px(it.textSize)
                        ds.typeface = it.typeface
                    }
                }, 0, s.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            con.append(s)
        }
    }
    this.text = con
}