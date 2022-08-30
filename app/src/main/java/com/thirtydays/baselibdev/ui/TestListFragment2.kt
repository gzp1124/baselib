package com.thirtydays.baselibdev.ui

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseListFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.adapter.TestDataBindingAdapter
import com.thirtydays.baselibdev.vm.TestListViewModel

/**
 * 测试使用 BaseListFragment 来快速创建 列表页面
 */
@Route(path = "/test/list2")
class TestListFragment2:BaseListFragment<String>() {
    @VMScope
    lateinit var viewModel: TestListViewModel
    val adapter = TestDataBindingAdapter()

    override fun createAdater() = adapter

    override fun setLayoutManager() = LinearLayoutManager(context)

    override fun setViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun showData() {
        viewModel.dataList.observe(this){
            adapter.setList(it)
        }
    }

    override fun setBackgroundResource(): Int {
        return R.color.xui_config_color_red
    }

    override fun setHeadTitleView() = TextView(context).apply {
        text = "这是顶部文字"
        setTextColor(Color.BLUE)
    }

    override fun setBottomView()= TextView(context).apply {
        text = "这是ddddddddddddd文字"
        setTextColor(Color.BLUE)
    }
}