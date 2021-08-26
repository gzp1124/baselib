package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestErrorDataBinding
import com.thirtydays.baselibdev.router.startFragment
import com.thirtydays.baselibdev.vm.TestViewModel

@Route(path = "/test/errordata_edit")
class TestErrorDataEditFragment:BaseVmFragment<FragmentTestErrorDataBinding>(R.layout.fragment_test_error_data) {

    @VMScope("TestErrorData") lateinit var testViewModel: TestViewModel

    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.vm = testViewModel
        mDataBinding.openPageBtn.text = "编辑文字"
        mDataBinding.openPageBtn.click {
            testViewModel.testData.postValue("这是来自编辑页面的数据")
        }
    }
}