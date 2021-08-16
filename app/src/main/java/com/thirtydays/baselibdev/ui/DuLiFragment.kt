package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestBinding
import com.thirtydays.baselibdev.vm.MainViewModel

/**
 * 演示使用 CommonActivity 直接打开单独的 Fragment
 */
@Route(path = "/test/duli")
class DuLiFragment : BaseVmFragment<FragmentTestBinding>(R.layout.fragment_test){

    @VMScope("MainActivity") lateinit var mViewModel:MainViewModel

    // 这种方式获取不到值，只能通过 Bundle 传值
    // @Autowired @JvmField var testDD:String? = ""

    override fun requestData() {
        mDataBinding.mDesc.text = "这里是独立fragment"+arguments?.getString("testDD")
    }



    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
    }
}