package com.aligit.base.framework.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aligit.base.Settings
import com.aligit.base.ext.coroutine.Block
import com.aligit.base.ext.coroutine.launchUI
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.model.CoroutineState

class NoViewModel : BaseViewModel() {
}

abstract class BaseViewModel : ViewModel() {
    //loading 框中的提示语
    var statusInfoStr = ""

    /**
     * 协程状态管理
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }
    val error = MutableLiveData<BaseThrowable>()

    /**
     * @param show 是否展示 loading 框
     * @param statusInfoStr loading 框中的提示语
     * @param block 请求体
     */
    fun launch(showLoading: Boolean = true, loadingInfoStr: String = "", block: Block) =
        launchUI {
            try {
                this@BaseViewModel.statusInfoStr = loadingInfoStr
                if (showLoading) statusLiveData.postValue(CoroutineState.Loading)
                block()
                if (showLoading) statusLiveData.postValue(CoroutineState.Finish)
            } catch (e: Exception) {
                if (showLoading) statusLiveData.postValue(CoroutineState.Error)
                error.postValue(BaseThrowable.ExternalThrowable(e))
            }
        }

//=======================
//===分页加载
    val page = MutableLiveData<Int>()
    val refreshing = MutableLiveData<Boolean>()
    val moreLoading = MutableLiveData<Boolean>()
    val hasMore = MutableLiveData<Boolean>()
    val autoRefresh = MutableLiveData<Boolean>()//SmartRefreshLayout自动刷新标记

    open fun loadMore() {
        page.value = (page.value ?: Settings.pageStartIndex) + 1
        moreLoading.value = true
    }

    open fun refresh() {
        page.value = Settings.pageStartIndex
        refreshing.value = true
    }

    fun autoRefresh() {
        autoRefresh.value = true
    }
}