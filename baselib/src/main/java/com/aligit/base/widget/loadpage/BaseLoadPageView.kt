package com.aligit.base.widget.loadpage

import android.content.Context
import android.view.View

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
enum class LoadPageStatus {
    Nromal, Loading, Fail, Empty, NoNet
}

abstract class BasePageViewForStatus {
    /**
     * 根布局
     */
    abstract fun getRootView(context: Context): View

    /**
     * 布局中的 加载更多视图
     */
    abstract fun getLoadingView(status: LoadPageViewForStatus): View

    /**
     * 布局中的 加载失败布局
     */
    abstract fun getLoadFailView(status: LoadPageViewForStatus): View

    /**
     * 布局中的 加载空布局
     */
    abstract fun getLoadEmptyView(status: LoadPageViewForStatus): View

    /**
     * 布局中的 加载无网络布局
     */
    abstract fun getLoadNoNetView(status: LoadPageViewForStatus): View


    /**
     * 可重写此方式，实行自定义逻辑
     * @param status BaseViewHolder
     * @param loadPageStatus loadPageStatus
     */
    open fun convert(status: LoadPageViewForStatus,needHideViews: MutableList<View>, loadPageStatus: LoadPageStatus) {
        status.isVisible(true)
        needHideViews.forEach { it.isVisible(false) }
        when (loadPageStatus) {
            LoadPageStatus.Loading -> {
                getLoadingView(status).isVisible(true)
                getLoadFailView(status).isVisible(false)
                getLoadEmptyView(status).isVisible(false)
                getLoadNoNetView(status).isVisible(false)
            }
            LoadPageStatus.Fail -> {
                getLoadingView(status).isVisible(false)
                getLoadFailView(status).isVisible(true)
                getLoadEmptyView(status).isVisible(false)
                getLoadNoNetView(status).isVisible(false)
            }
            LoadPageStatus.Empty -> {
                getLoadingView(status).isVisible(false)
                getLoadFailView(status).isVisible(false)
                getLoadEmptyView(status).isVisible(true)
                getLoadNoNetView(status).isVisible(false)
            }
            LoadPageStatus.NoNet -> {
                getLoadingView(status).isVisible(false)
                getLoadFailView(status).isVisible(false)
                getLoadEmptyView(status).isVisible(false)
                getLoadNoNetView(status).isVisible(true)
            }
            else -> {
                getLoadingView(status).isVisible(false)
                getLoadFailView(status).isVisible(false)
                getLoadEmptyView(status).isVisible(false)
                getLoadNoNetView(status).isVisible(false)
                status.isVisible(false)
                needHideViews.forEach { it.isVisible(true) }
            }
        }
    }

    private fun View.isVisible(visible: Boolean) {
        this.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}