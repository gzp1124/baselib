package com.thirtydays.baselibdev.ui

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.databinding.TestBottomView2Binding
import com.aligit.base.databinding.TestBottomViewBinding
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.model.BasePageBean
import com.aligit.base.ui.fragment.BaseListFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.adapter.TestDataBindingAdapter
import com.thirtydays.baselibdev.vm.TestListViewModel

/**
 * 测试使用 BaseListFragment 来快速创建 列表页面
 */
@Route(path = "/test/list2")
class TestListFragment2 : BaseListFragment<String>() {
    @VMScope
    lateinit var viewModel: TestListViewModel
    val adapter = TestDataBindingAdapter()

    override fun createAdater() = adapter

    override fun setLayoutManager() = LinearLayoutManager(context)

    override fun setViewModel(): BaseViewModel? {
        return viewModel
    }


    override fun setListLiveData(): LiveData<BasePageBean<List<String>?>?>? {
//        return viewModel.dataList
        /*
        // 等价与下面的写法
        viewModel.dataList.observe(this) {
            if (it == null) return@observe
            if (it.page == Settings.Request.pageStartIndex) {
                mAdapter.setList(it.data)
            } else {
                it.data?.let { it1 -> mAdapter.addData(it1) }
            }
        }
         */
        return null
    }

    override fun showData() {
        viewModel.dataList.observe(this) {
            adapter.setList(it)
        }
    }

    override fun setBackgroundResource(): Int {
        return R.color.xui_config_color_red
    }

    override fun headTitleViewIsAlwaysTop(): Boolean {
        return false
    }

    override fun setHeadTitleView(): List<View> {
        return mutableListOf(
            // 使用 databinding 的方式
            DataBindingUtil.bind<TestBottomViewBinding>(
                TestBottomViewBinding.inflate(layoutInflater).root
            )!!.apply {
                name = "使用 databinding 的方式，设置了内容1"
            }.root,
            // 使用 viewbinding 的方式
            TestBottomView2Binding.inflate(layoutInflater).apply {
                tv.text = "使用 viewbinding 的方式，设置了内容2"
            }.root,
        )
    }

    override fun setBottomView(): List<View> {
        return mutableListOf(
            TextView(context).apply {
                text = "这是ddddddddddddd文字"
                setTextColor(Color.BLUE)
            },
            TextView(context).apply {
                text = "这是ddddddddddddd文字2222"
                setTextColor(Color.BLUE)
            }
        )
    }

}