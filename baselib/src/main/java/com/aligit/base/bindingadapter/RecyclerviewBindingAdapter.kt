package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

@BindingAdapter(value = ["gSetAdapter", "gSetList"], requireAll = false)
fun <T> gSetAdapter(view: RecyclerView, adapter: BaseQuickAdapter<T,out BaseViewHolder>, list: List<T>) {
}