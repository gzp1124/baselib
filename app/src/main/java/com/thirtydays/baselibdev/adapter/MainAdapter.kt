package com.thirtydays.baselibdev.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.ext.startCommonFragment
import com.aligit.base.ext.view.click
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ItemMainBinding
import com.thirtydays.baselibdev.net.bean.MainBean

class MainAdapter:BaseQuickAdapter<MainBean,BaseDataBindingHolder<ItemMainBinding>>(R.layout.item_main) {
    override fun convert(holder: BaseDataBindingHolder<ItemMainBinding>, item: MainBean) {
        holder.dataBinding.also {
            it?.name = item.name
            it?.root?.click {
                if (item.isFragment){
                    startCommonFragment(item.pagePath)
                }else{
                    ARouter.getInstance().build(item.pagePath).navigation()
                }
            }
        }
    }
}