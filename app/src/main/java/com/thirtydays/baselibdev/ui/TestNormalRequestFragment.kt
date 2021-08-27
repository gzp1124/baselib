package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestNormalRequestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

@Route(path = "/test/normal_request")
class TestNormalRequestFragment:BaseVmFragment<FragmentTestNormalRequestBinding>(R.layout.fragment_test_normal_request) {

    @VMScope lateinit var mainViewModel: MainViewModel

    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.vm = mainViewModel
        mDataBinding.openPageBtn.click {
//            mainViewModel.reqData.postValue("")
            mainViewModel.requestData2()
        }
    }
}