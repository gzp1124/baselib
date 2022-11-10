package com.aligit.base.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.databinding.FragmentListBinding
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.model.BasePageBean
import com.chad.library.adapter.base.BaseQuickAdapter

interface ListDataReq {
    fun onLoadMore()
    fun onRefresh()
}

abstract class BaseListFragment<B> : BaseVmFragment<FragmentListBinding>(R.layout.fragment_list),
    ListDataReq {

    abstract fun createAdater(): BaseQuickAdapter<B, *>

    abstract fun setLayoutManager(): RecyclerView.LayoutManager

    abstract fun setViewModel(): BaseViewModel?

    /**
     * setListLiveData 和 showData 都是用于给 list 提供数据源，设置一个即可
     */
    abstract fun setListLiveData(): LiveData<BasePageBean<List<B>?>?>?

    /**
     * setListLiveData 和 showData 都是用于给 list 提供数据源，设置一个即可
     */
    abstract fun showData()

    open fun canLoadMore(): Boolean = true
    open fun canRefresh(): Boolean = true

    override fun onLoadMore() {
        vm?.loadMore()
    }

    override fun onRefresh() {
        vm?.refresh()
    }

    val mAdapter: BaseQuickAdapter<B, *> by lazy { createAdater() }
    val vm: BaseViewModel? by lazy { setViewModel() }

    override fun onInitDataBinding() {
        mDataBinding.vm = vm
        vm?.page?.postValue(Settings.Request.pageStartIndex)
        mDataBinding.headLin.removeAllViews()
        if (headTitleViewIsAlwaysTop()) {
            setHeadTitleView()?.forEach { mDataBinding.headLin.addView(it) }
        }
        mDataBinding.bottomLin.removeAllViews()
        if (bottomViewIsAlwaysBottom()) {
            setBottomView()?.forEach { mDataBinding.bottomLin.addView(it) }
        }
        mDataBinding.rootVV.setBackgroundResource(setBackgroundResource())
        mDataBinding.recyclerView.also {
            it.adapter = mAdapter.apply {
                removeAllHeaderView()
                removeAllFooterView()
                if (!headTitleViewIsAlwaysTop()) {
                    setHeadTitleView()?.forEach { it1 -> addHeaderView(it1) }
                }
                if (!bottomViewIsAlwaysBottom()) {
                    setBottomView()?.forEach { it1 -> addFooterView(it1) }
                }
            }
            it.layoutManager = setLayoutManager()
        }
        mDataBinding.smartRefreshLayout.also {
            val haveVm = (mDataBinding.vm != null)
            it.setEnableLoadMore(haveVm && canLoadMore())
            it.setEnableRefresh(haveVm && canRefresh())
            it.setEnableOverScrollDrag(true)
        }

        setListLiveData()?.observe(this) {
            if (it == null) return@observe
            if (it.page == Settings.Request.pageStartIndex) {
                mAdapter.setList(it.data)
            } else {
                it.data?.let { it1 -> mAdapter.addData(it1) }
            }
        }
        showData()
    }

    open fun headTitleViewIsAlwaysTop(): Boolean = true
    open fun setHeadTitleView(): List<View>? {
        return null
    }

    open fun bottomViewIsAlwaysBottom(): Boolean = true
    open fun setBottomView(): List<View>? {
        /*
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
        */
        return null
    }

    open fun setBackgroundResource(): Int {
        return R.color.base_white
    }
}