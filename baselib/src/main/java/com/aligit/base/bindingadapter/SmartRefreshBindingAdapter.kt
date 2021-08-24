package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import com.aligit.base.ext.tool.log
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener


@BindingAdapter(
    value = ["bindRefreshing", "bindMoreLoading", "bindHasMore"],
    requireAll = false
)
fun bindSmartRefreshLayout(
    smartLayout: SmartRefreshLayout,
    refreshing: Boolean,
    moreLoading: Boolean,
    hasMore: Boolean?
) {
    if (!refreshing) {
        smartLayout.finishRefresh()
    }
    if (!moreLoading) {
        smartLayout.finishLoadMore()
    }
    if (hasMore != null)
        smartLayout.setNoMoreData(!hasMore)//调用次方法会停止刷新动作
}


@BindingAdapter(
    value = ["bindAutoRefresh"]
)
fun bindSmartRefreshLayout(
    smartLayout: SmartRefreshLayout,
    autoRefresh: Boolean
) {
    if (autoRefresh) smartLayout.autoRefresh()
}

@BindingAdapter(
    value = ["bindOnRefreshListener", "bindOnLoadMoreListener"],
    requireAll = false
)
fun bindListener(
    smartLayout: SmartRefreshLayout,
    refreshListener: OnRefreshListener?,
    loadMoreListener: OnLoadMoreListener?
) {
    smartLayout.setOnRefreshListener(refreshListener)
    smartLayout.setOnLoadMoreListener(loadMoreListener)
}