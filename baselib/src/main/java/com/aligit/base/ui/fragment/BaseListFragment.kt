package com.aligit.base.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.aligit.base.R
import com.aligit.base.databinding.FragmentListBinding
import com.aligit.base.framework.mvvm.BaseViewModel
import com.chad.library.adapter.base.BaseQuickAdapter

abstract class BaseListFragment<B> : BaseVmFragment<FragmentListBinding>(R.layout.fragment_list) {

    abstract fun createAdater(): BaseQuickAdapter<B,*>

    abstract fun setLayoutManager(): RecyclerView.LayoutManager

    abstract fun setViewModel() : BaseViewModel?

    abstract fun showData()

    fun canLoadMore():Boolean = true
    fun canRefresh():Boolean = true

    val mAdapter : BaseQuickAdapter<B,*> by lazy { createAdater() }

    override fun onInitDataBinding() {
        mDataBinding.vm = setViewModel()
        mDataBinding.recyclerView.also {
            it.adapter = mAdapter
            it.layoutManager = setLayoutManager()
        }
        mDataBinding.smartRefreshLayout.also {
            val haveVm = (mDataBinding.vm != null)
            it.setEnableLoadMore(haveVm && canLoadMore())
            it.setEnableRefresh(haveVm && canRefresh())
            it.setEnableOverScrollDrag(true)
        }

        showData()
    }
}