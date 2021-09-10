package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestConvertBinding
import com.thirtydays.baselibdev.vm.TestConvertViewModel

/*
直接在 data class 中定义方法进行字段转换即可

 */
@Route(path = "/test/test_convert")
class TestConvertFragment:BaseVmFragment<FragmentTestConvertBinding>(R.layout.fragment_test_convert) {
    @VMScope lateinit var testConvertViewModel: TestConvertViewModel

    override fun onInitDataBinding() {
        mDataBinding.vm = testConvertViewModel
        mDataBinding.req.click {
            testConvertViewModel.refreshTrigger.postValue(true)
        }
    }

}