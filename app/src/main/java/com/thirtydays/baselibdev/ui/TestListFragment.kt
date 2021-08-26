package com.thirtydays.baselibdev.ui

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.adapter.TestDataBindingAdapter
import com.thirtydays.baselibdev.databinding.FragmentTestListBinding
import com.thirtydays.baselibdev.vm.TestListViewModel

@Route(path = "/test/list")
class TestListFragment:BaseVmFragment<FragmentTestListBinding>(R.layout.fragment_test_list) {

    @VMScope lateinit var viewModel: TestListViewModel
    val adapter = TestDataBindingAdapter()


    override fun requestData() { }

    override fun onInitDataBinding() {
        mDataBinding.vm = viewModel

        initRecyclerView()



        Log.e("gzp112411","时间："+System.currentTimeMillis())
        Log.e("gzp112411","渠道的结果是："+MainActivity.test.ll.size)
        Log.e("gzp112411","时间--："+System.currentTimeMillis())
    }

    private fun initRecyclerView() {
        mDataBinding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
        viewModel.dataList.observe(this){
            adapter.setList(it)
        }

    }
}