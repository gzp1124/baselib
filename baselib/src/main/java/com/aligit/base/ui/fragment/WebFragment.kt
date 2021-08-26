package com.aligit.base.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.R
import com.aligit.base.databinding.FragmentWebBinding

@Route(path = "/common/web")
class WebFragment:BaseVmFragment<FragmentWebBinding>(R.layout.fragment_web) {
    override fun onInitDataBinding() {
    }

    override fun requestData() {
    }
}