package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.tool.toast
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentFlowTestBinding
import com.thirtydays.baselibdev.vm.TestFlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Route(path = "/test/flow")
class TestFlowFragment:BaseVmFragment<FragmentFlowTestBinding>(R.layout.fragment_flow_test) {

    @VMScope lateinit var testFlowViewModel: TestFlowViewModel

    override fun requestData() {
    }

    @ExperimentalCoroutinesApi
    override fun onInitDataBinding() {
        mDataBinding.vm = testFlowViewModel
        testFlowViewModel.refreshChannel.offer(true)

        toast { "这个还没有完成，以后有空在研究" }
    }
}