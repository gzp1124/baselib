package com.aligit.base.bindingadapter

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.databinding.BindingAdapter
import com.aligit.base.ext.tool.dp2px


@BindingAdapter(value = ["gone"])
fun bindGone(v: View, gone: Boolean) {
    v.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter(value = ["invisible"])
fun bindInvisible(v: View, invisible: Boolean) {
    v.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(value = ["isSelectClick"])
fun bindIsSelectClick(v: View, isSelect: Boolean) {
    v.isSelected = isSelect
    v.isClickable = isSelect
}

@BindingAdapter(value = ["isOnlySelect"])
fun bindIsOnlySelect(v: View, isSelect: Boolean) {
    v.isSelected = isSelect
}

private fun getLp(view: View): ViewGroup.MarginLayoutParams? =
    view.layoutParams as? ViewGroup.MarginLayoutParams

@BindingAdapter("gMarginStart")
fun gMarginStart(view: View, margin: Number) {
    getLp(view)?.marginStart = dp2px(margin.toInt())
}

@BindingAdapter("gMarginEnd")
fun gMarginEnd(view: View, margin: Number) {
    getLp(view)?.marginEnd = dp2px(margin.toInt())
}

@BindingAdapter("gMarginTop")
fun gtopMargin(view: View, margin: Number) {
    getLp(view)?.topMargin = dp2px(margin.toInt())
}

@BindingAdapter("gMarginBottom")
fun gMarginBottom(view: View, margin: Number) {
    getLp(view)?.bottomMargin = dp2px(margin.toInt())
}

@BindingAdapter("gMarginLeft")
fun gMarginLeft(view: View, margin: Number) {
    getLp(view)?.leftMargin = dp2px(margin.toInt())
}

@BindingAdapter("gMarginRight")
fun gMarginRight(view: View, margin: Number) {
    getLp(view)?.rightMargin = dp2px(margin.toInt())
}

@BindingAdapter("onBackPressed")
fun bindOnBackPressed(view: View, onBackPress: Boolean) {
    val context = view.context
    if (onBackPress && context is OnBackPressedDispatcherOwner) {
        view.setOnClickListener {
            context.onBackPressedDispatcher.onBackPressed()
        }
    }else{
        view.setOnClickListener(null)
    }
}

