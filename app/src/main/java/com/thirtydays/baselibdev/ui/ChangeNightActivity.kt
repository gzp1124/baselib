package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.tool.changDarkMode
import com.aligit.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.click.ChangeNightClick
import com.thirtydays.baselibdev.databinding.ActivityChangeNightBinding

@Route(path = "/change/lang")
class ChangeNightActivity: BaseVmActivity<ActivityChangeNightBinding>(R.layout.activity_change_night)
    ,ChangeNightClick{
    override fun onInitDataBinding() {
        mDataBinding.click = this
    }

    override fun initData() {
    }

    override fun changeNight(mode: Int) {
        changDarkMode(mode)
    }
}