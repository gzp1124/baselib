package com.thirtydays.baselibdev.ui

import android.graphics.Color
import com.aligit.base.ext.tool.getResColor
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestImmersionBarBinding

class TestImmersionBarFragment:BaseVmFragment<FragmentTestImmersionBarBinding>(R.layout.fragment_test_immersion_bar) {
    override fun onInitDataBinding() {
        mDataBinding.tvv.apply {
            setBackgroundColor(Color.parseColor("#00ff00"))
            text = "1111111111111"
            setTextColor(getResColor(R.color.white))
        }
    }
}