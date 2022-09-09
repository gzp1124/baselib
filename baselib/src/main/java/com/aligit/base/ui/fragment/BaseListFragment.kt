package com.aligit.base.ui.fragment

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.aligit.base.R
import com.aligit.base.databinding.FragmentListBinding
import com.aligit.base.ext.tool.getResColor
import com.aligit.base.ext.tool.getResDrawable
import com.aligit.base.framework.mvvm.BaseViewModel
import com.chad.library.adapter.base.BaseQuickAdapter

interface ListDataReq{
    fun onLoadMore()
    fun onRefresh()
}

abstract class BaseListFragment<B> : BaseVmFragment<FragmentListBinding>(R.layout.fragment_list),ListDataReq {

    abstract fun createAdater(): BaseQuickAdapter<B,*>

    abstract fun setLayoutManager(): RecyclerView.LayoutManager

    abstract fun setViewModel() : BaseViewModel?

    abstract fun showData()

    fun canLoadMore():Boolean = true
    fun canRefresh():Boolean = true

    override fun onLoadMore(){
        vm?.loadMore()
    }

    override fun onRefresh() {
        vm?.refresh()
    }

    val mAdapter : BaseQuickAdapter<B,*> by lazy { createAdater() }
    val vm : BaseViewModel? by lazy { setViewModel() }

    override fun onInitDataBinding() {
        mDataBinding.vm = vm
        setHeadTitleView()?.let { mDataBinding.headLin.addView(it) }
        setBottomView()?.let { mDataBinding.bottomLin.addView(it) }
        mDataBinding.rootVV.setBackgroundResource(setBackgroundResource())
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

    open fun setHeadTitleView(): View? {
        return null
    }
    open fun setBottomView(): View? {
        return null
    }
    open fun setBackgroundResource(): Int {
        return R.color.base_white
    }
}