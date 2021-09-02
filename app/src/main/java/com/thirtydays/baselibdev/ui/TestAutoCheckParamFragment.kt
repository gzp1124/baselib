package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentAutoCheckParamBinding
import com.thirtydays.baselibdev.vm.TestViewModel

/**
 * 测试参数自动校验
 */
@Route(path = "/test/auto_check_param")
class TestAutoCheckParamFragment:BaseVmFragment<FragmentAutoCheckParamBinding>(R.layout.fragment_auto_check_param) {
    @VMScope lateinit var testViewModel: TestViewModel
    override fun onInitDataBinding() {
        mDataBinding.vm = testViewModel
    }

    override fun requestData() {
    }
}