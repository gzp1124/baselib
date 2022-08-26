package com.thirtydays.baselibdev.ui

import android.graphics.Color
import com.aligit.base.ext.tool.getResColor
import com.aligit.base.ui.fragment.BaseVmFragment
import com.gyf.immersionbar.ktx.immersionBar
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestImmersionBarBinding

class TestImmersionBarFragment2:BaseVmFragment<FragmentTestImmersionBarBinding>(R.layout.fragment_test_immersion_bar) {
    override fun onInitDataBinding() {
        immersionBar {
            statusBarView(mDataBinding.topV)
        }

        mDataBinding.tvv.apply {
            setBackgroundColor(Color.parseColor("#ff0000"))
            text = "第二个页面"
            setTextColor(getResColor(R.color.white))
        }
    }
}