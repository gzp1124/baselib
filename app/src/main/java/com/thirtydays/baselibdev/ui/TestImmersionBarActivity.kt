package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.ui.activity.BaseVmActivity
import com.blankj.utilcode.util.FragmentUtils
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityTestImmersionBarBinding

@Route(path = "/test/immersionBar")
class TestImmersionBarActivity:BaseVmActivity<ActivityTestImmersionBarBinding>(R.layout.activity_test_immersion_bar) {
    override fun isImmersionBar() = true
    override fun onInitDataBinding() {
    }

    override fun initData() {
        FragmentUtils.add(
            supportFragmentManager,
            f1,
            R.id.contentFrame,
            "fragment_1"
        )
        FragmentUtils.add(
            supportFragmentManager,
            f2,
            R.id.contentFrame,
            "fragment_2"
        )
        mDataBinding.page1.click { switchF(1) }
        mDataBinding.page2.click { switchF(2) }
        switchF(1)
    }

    val f1 = TestImmersionBarFragment()
    val f2 = TestImmersionBarFragment2()

    fun switchF(index:Int){
        FragmentUtils.hide(if(index==1) f2 else f1)
        FragmentUtils.show(if(index==1) f1 else f2)
    }
}