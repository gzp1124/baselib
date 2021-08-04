package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.framework.mvvm.scope.VMScope
import com.seabreeze.robot.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

@Route(path = "/test/duli")
class DuLiFragment : BaseVmFragment<FragmentTestBinding>(R.layout.fragment_test){

    @VMScope("MainActivity") lateinit var mViewModel:MainViewModel

    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
    }
}