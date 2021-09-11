package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestCicleProgressBinding
import com.thirtydays.baselibdev.vm.TestViewModel

@Route(path = "/test/cicle_progress")
class TestCicleProgressFragment:BaseVmFragment<FragmentTestCicleProgressBinding>(R.layout.fragment_test_cicle_progress) {
    @VMScope lateinit var testViewModel: TestViewModel
    override fun onInitDataBinding() {
        mDataBinding.vm = testViewModel
        mDataBinding.add.click {
            var t = testViewModel.testProgress.value ?: 0
            t += 10
            if (t > 100) t = 100
            testViewModel.testProgress.postValue(t)
        }
        mDataBinding.jian.click {
            var t = testViewModel.testProgress.value ?: 0
            t -= 10
            if (t < 0) t = 0
            testViewModel.testProgress.postValue(t)
        }
    }
}