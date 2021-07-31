package com.thirtydays.baselibdev.ui

import com.seabreeze.robot.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

class Test2Fragment: BaseVmFragment<MainViewModel,FragmentTestBinding>(R.layout.fragment_test) {
    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
    }

    override fun requestData() {
    }
}