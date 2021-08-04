package com.thirtydays.baselibdev.ui

import android.util.Log
import com.seabreeze.robot.base.framework.mvvm.scope.VMScope
import com.seabreeze.robot.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

class TestFragment: BaseVmFragment<FragmentTestBinding>(R.layout.fragment_test) {

    @VMScope("TestFragment") lateinit var mViewModel: MainViewModel

    override fun requestData() {
        mViewModel.getTime()
    }

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
//        mViewModel.testContent.observe(this){
//
//        }
    }
}