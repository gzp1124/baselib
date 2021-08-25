package com.aligit.base.framework.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aligit.base.Settings
import com.aligit.base.ext.coroutine.Block
import com.aligit.base.ext.coroutine.launchUI
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.model.CoroutineState
import com.aligit.base.net.livedata_api.IResponse
import com.aligit.base.widget.loadpage.LoadPageStatus

class NoViewModel : BaseViewModel() {
}

abstract class BaseViewModel : ViewModel() {
    /**
     * 协程状态管理
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }
    val error = MutableLiveData<BaseThrowable>()

    /**
     * @param show 是否展示 loading 框
     * @param block 请求体
     */
    fun launch(showLoading: Boolean = true, block: Block) =
        launchUI {
            try {
                val loading = CoroutineState.Loading
                if (showLoading) statusLiveData.postValue(loading)
                block()
                if (showLoading) statusLiveData.postValue(CoroutineState.Finish)
            } catch (e: Exception) {
                if (showLoading) statusLiveData.postValue(CoroutineState.Error)
                error.postValue(BaseThrowable.ExternalThrowable(e))
            }
        }

    //=======================
    // 页面状态管理
    /**
     * 页面状态：刷新加载布局常用封装
     */
    val loadPageLiveData: MutableLiveData<LoadPageStatus> by lazy {
        MutableLiveData<LoadPageStatus>()
    }

    //=======================
    //刷新触发器，如果不是共享的 ViewModel，那么在界面初始化完后会自动设置该值
    //如果 ViewModel 的修饰符 @VMScope 没有在括号中指定 Vm scope 的 key 值，则认为非共享 ViewModel ，则进入页面就修改该值
    //如果 ViewModel 的修饰符 @VMScope("userModel") 中指定了 key 则认为是共享 ViewModel ，进入页面默认不进行请求。
    //如果要进行请求，修改 refreshTrigger 的值，或者修改 page 的值，则会触发 requestData 或 requestListData （分别对应普通接口和列表接口）
    //只控制普通请求，列表请求使用 page 进行控制
    val refreshTrigger = MutableLiveData<Boolean>()

    //===分页加载
    val page = MutableLiveData<Int>()
    val refreshing = MutableLiveData<Boolean>()
    val moreLoading = MutableLiveData<Boolean>()
    val hasMore = MutableLiveData<Boolean>()

    // 列表数据加载更多
    fun loadMore() {
        page.value = (page.value ?: Settings.pageStartIndex) + 1
        moreLoading.value = true
    }

    // 刷新列表数据
    fun refresh() {
        page.value = Settings.pageStartIndex
        refreshing.value = true
    }

    // 重新加载页面数据
    fun reload(){
        refresh()
        refreshTrigger.postValue(true)
    }

    /**
     * 普通接口请求
     * @param reqBolck 请求网络获取数据的方法体
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语
     * @param watchTag 监听该字段，自动请求接口
     * @param parseBolck 处理数据的方法体，该方法的返回值将作为 LiveData 对外提供
     */
    fun <R, Y, T : IResponse<Y>> requestData(
        reqBolck: () -> LiveData<T>,
        showLoading: Boolean = true,
        loadingTips: String? = "",
        watchTag: MutableLiveData<*> = refreshTrigger,
        parseBolck: (Y?) -> R
    ): LiveData<R> {
        return Transformations.map(
            Transformations.switchMap(watchTag) {
                if (showLoading) {
                    val loading = CoroutineState.Loading
                    loading.loadingTips = loadingTips
                    statusLiveData.postValue(loading)
                }
                reqBolck()
            }
        ) {
            if (showLoading) statusLiveData.postValue(if (true == it.resultStatus) CoroutineState.Error else CoroutineState.Finish)
            parseBolck(it.resultData1)
        }
    }

    /**
     * 列表页面请求，监听 page 自动请求列表接口
     * @param reqBolck 网络请求的方法体
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语
     * @param parseBolck 处理响应的数据
     */
    fun <R, Y, T : IResponse<Y>> requestListData(
        reqBolck: () -> LiveData<T>,
        showLoading: Boolean = true,
        loadingTips: String? = "",
        parseBolck: (Y?) -> R
    ): LiveData<R> {
        return Transformations.map(
            Transformations.switchMap(page) {
                if (showLoading) {
                    val loading = CoroutineState.Loading
                    loading.loadingTips = loadingTips
                    statusLiveData.postValue(loading)
                }
                reqBolck()
            }
        ) {
            if (showLoading) statusLiveData.postValue(if (true == it.resultStatus) CoroutineState.Finish else CoroutineState.Error)
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = it.hasMoreData()
            parseBolck(it.resultData1)
        }
    }
}