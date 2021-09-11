package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener


@BindingAdapter(
    value = ["gRefreshing", "gMoreLoading", "gHasMore"],
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
    value = ["gCanLoadMore", "gCanRefresh", "gCanDrag"], requireAll = false
)
fun bindSmartCan(
    smartLayout: SmartRefreshLayout,
    canLoadMore: Boolean = true,
    canRefresh: Boolean = true,
    canDrag: Boolean = true
) {
    smartLayout.setEnableLoadMore(canLoadMore)
    smartLayout.setEnableRefresh(canRefresh)
    smartLayout.setEnableOverScrollDrag(canDrag)
}

@BindingAdapter(
    value = ["gAutoRefresh"]
)
fun bindSmartRefreshLayout(
    smartLayout: SmartRefreshLayout,
    autoRefresh: Boolean
) {
    if (autoRefresh) smartLayout.autoRefresh()
}

@BindingAdapter(
    value = ["gOnRefreshListener", "gOnLoadMoreListener"],
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