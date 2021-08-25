package com.thirtydays.baselibdev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ItemTestListBinding

class TestDataBindingAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<ItemTestListBinding>>(R.layout.item_test_list) {
    override fun convert(holder: BaseDataBindingHolder<ItemTestListBinding>, item: String) {
        holder.dataBinding?.s = item
    }

}