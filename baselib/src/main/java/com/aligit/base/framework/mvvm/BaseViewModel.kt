package com.aligit.base.framework.mvvm

import android.os.SystemClock
import androidx.lifecycle.*
import com.aligit.base.Settings
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.model.CoroutineState
import com.aligit.base.net.livedata_api.IResponse
import com.aligit.base.widget.loadpage.LoadPageStatus
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoViewModel : BaseViewModel() {
}

abstract class BaseViewModel : ViewModel() {
    /**
     * 协程状态管理
     */
    val statusLiveData: UnPeekLiveData<CoroutineState> by lazy {
        UnPeekLiveData<CoroutineState>()
    }
    val error = UnPeekLiveData<BaseThrowable>()


    //=======================
    // 页面状态管理
    /**
     * 页面状态：刷新加载布局常用封装
     */
    val loadPageLiveData: UnPeekLiveData<LoadPageStatus> by lazy {
        UnPeekLiveData<LoadPageStatus>()
    }

    //=======================
    //刷新触发器，如果不是共享的 ViewModel，那么在界面初始化完后会自动设置该值
    //如果 ViewModel 的修饰符 @VMScope 没有在括号中指定 Vm scope 的 key 值，则认为非共享 ViewModel ，则进入页面就修改该值
    //如果 ViewModel 的修饰符 @VMScope("userModel") 中指定了 key 则认为是共享 ViewModel ，进入页面默认不进行请求。
    //如果要进行请求，修改 refreshTrigger 的值，或者修改 page 的值，则会触发 requestData 或 requestListData （分别对应普通接口和列表接口）
    //只控制普通请求，列表请求使用 page 进行控制
    val refreshTrigger = UnPeekLiveData<Boolean>()

    //===分页加载
    val page = UnPeekLiveData<Int>()
    val refreshing = UnPeekLiveData<Boolean>()
    val moreLoading = UnPeekLiveData<Boolean>()
    val hasMore = UnPeekLiveData<Boolean>()

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
    fun reload() {
        refresh()
        refreshTrigger.postValue(true)
    }


    //===========数据请求=========
    // 对 flow 进行统一处理
    private fun <T> parseRequest(
        flow: Flow<T>,
        showLoading: Boolean = true,
        loadingTips: String? = null,
    ): Flow<T> {
        return flow
            .onStart {
                val loading = CoroutineState.Loading
                if (loadingTips != null) loading.loadingTips = loadingTips
                if (showLoading) statusLiveData.postValue(loading)
            }
            .onCompletion {
                if (showLoading) statusLiveData.postValue(CoroutineState.Finish)
            }
            .catch { e ->
                e.printStackTrace()
                if (showLoading) statusLiveData.postValue(CoroutineState.Error)
                val e1 = BaseThrowable.ExternalThrowable(e)
                error.postValue(e1)
            }
            .flowOn(Dispatchers.IO)
    }
    /**
     * 普通方式请求普通接口，不使用 livedata
     */
    fun <T> requestData(flow: Flow<T>, showLoading: Boolean = true, loadingTips: String? = null, parseBolck: (T) -> Unit){
        viewModelScope.launch {
            parseRequest(flow, showLoading,loadingTips).collect {
                parseBolck(it)
            }
        }
    }
    /**
     * 普通方式请求列表接口，不使用 livedata
     */
    fun <Y, T : IResponse<Y>> requestListData(flow: Flow<T>, showLoading: Boolean = true, loadingTips: String? = null, parseBolck: (T) -> Unit) {
        viewModelScope.launch {
            parseRequest(flow, showLoading,loadingTips).collect {
                refreshing.value = false
                moreLoading.value = false
                hasMore.value = it.hasMoreData()
                parseBolck(it)
            }
        }
    }
    /**
     * 普通接口请求
     * @param reqBolck 请求网络获取数据的方法体
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语（传 null 显示默认提示语，传 其他值则显示对应的值，传 空字符串 显示也为空字符串）
     * @param watchTag 监听该字段，自动请求接口
     * @param parseBolck 处理数据的方法体，该方法的返回值将作为 LiveData 对外提供
     *
     * 泛型说明
     *      R: 通过 parseBolck 方法将接口返回的原始类型 Y 处理为 R 类型，Y 和 R 可以是同一类型，可以是不同类型
     *      Y: 实际的业务数据，去掉 IResponse 后，原始接口返回的类型
     *      T: IResponse<R>，接口返回的包裹业务数据的
     */
    fun <R, Y, T : IResponse<Y>> requestDataToLiveData(
        reqBolck: Flow<T>,
        showLoading: Boolean = true,
        loadingTips: String? = null,
        watchTag: UnPeekLiveData<out Any> = refreshTrigger,
        parseBolck: (Y?) -> R
    ): LiveData<R> {
        return Transformations.map(
            Transformations.switchMap(watchTag) {
                parseRequest(reqBolck, showLoading, loadingTips).asLiveData()
            }
        ) {
            parseBolck(it.resultData)
        }
    }
    /**
     * 列表页面请求，监听 page 自动请求列表接口
     * @param reqBolck 网络请求的方法体
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语
     * @param parseBolck 处理响应的数据
     */
    fun <R, Y, T : IResponse<Y>> requestListDataToLiveData(
        reqBolck: Flow<T>,
        showLoading: Boolean = true,
        loadingTips: String? = null,
        parseBolck: (Y?) -> R
    ): LiveData<R> {
        return Transformations.map(
            Transformations.switchMap(page) {
                parseRequest(reqBolck, showLoading, loadingTips).asLiveData()
            }
        ) {
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = it.hasMoreData()
            parseBolck(it.resultData)
        }
    }
}