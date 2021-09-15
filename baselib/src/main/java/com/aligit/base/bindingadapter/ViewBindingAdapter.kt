package com.aligit.base.bindingadapter

import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.databinding.BindingAdapter


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

