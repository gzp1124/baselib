package com.thirtydays.baselibdev.ui

import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestRequestBinding
import com.thirtydays.baselibdev.vm.TestViewModel

class TestRequestFragment:BaseVmFragment<FragmentTestRequestBinding>(R.layout.fragment_test_request) {

    @VMScope lateinit var testViewModel: TestViewModel

    override fun onInitDataBinding() {
        mDataBinding.tv.text = arguments?.getString("title")
        mDataBinding.req.click {
            testViewModel.reqData.postValue("")
        }
        mDataBinding.req2.click {
            testViewModel.reqData2.postValue("")
        }

        // 只有 normalData1 被 observe 之后，其中的请求才会执行
        // normalData2 中的请求就不会执行
        // 使用 xml 直接获取 normalData1 中的数据也已经执行了 observe 操作
        testViewModel.normalData1.observe(this){
            mDataBinding.tv.text = it
        }
    }

    override fun requestData() {
    }
}