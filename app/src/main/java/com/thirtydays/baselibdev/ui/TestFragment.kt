package com.thirtydays.baselibdev.ui

import com.seabreeze.robot.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

class TestFragment: BaseVmFragment<MainViewModel, FragmentTestBinding>(R.layout.fragment_test) {

    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
//        mViewModel.testContent.observe(this){
//
//        }
    }
}