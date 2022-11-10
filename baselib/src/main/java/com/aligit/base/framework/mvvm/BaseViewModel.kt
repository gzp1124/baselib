package com.aligit.base.framework.mvvm

import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.aligit.base.Settings
import com.aligit.base.ext.dowithTry
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.tool.log
import com.aligit.base.model.BasePageBean
import com.aligit.base.model.CoroutineState
import com.aligit.base.net.livedata_api.IResponse
import com.aligit.base.widget.loadpage.LoadPageStatus
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
    open fun loadMore() {
        page.postValue(page.value!! + 1)
        moreLoading.postValue(true)
    }

    // 刷新列表数据
    open fun refresh() {
        page.postValue(Settings.Request.pageStartIndex)
        refreshing.postValue(true)
    }

    // 重新加载页面数据
    fun reload() {
        refresh()
        refreshTrigger.postValue(true)
    }


    //===========数据请求=========
    // ViewModel内通用方法：对请求体 flow 进行统一处理
    open fun <T> parseRequest(
        flow: Flow<T>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
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
                if (showLoading) statusLiveData.postValue(CoroutineState.Error)
                if (!ignoreCacheErr) catchErr(e, 1)
            }
            .flowOn(Dispatchers.IO)
    }

    // ViewModel内通用方法：过滤所有响应数据，处理通用业务，如 token 失效 账号被顶等情况
    // ViewModel 可以覆盖该方法，实现其他的业务需求，不会影响其他 ViewModel
    // 业务相关处理，对应 BaseThrowable.InsideThrowable
    // 返回值：false业务异常中止后续操作，true业务正常后续正常执行
    open fun <Y, T : IResponse<Y>> responseFilter(t: T?): Boolean {
        if (Settings.Request.tokenErrCode?.equals(t?.errorCode) == true) {
            error.postValue(BaseThrowable.TokenThrowable())
            return false
        }
        return true
    }

    // ViewModel内通用方法：捕获到异常的处理方法，默认处理方式为 error.postValue，交由 BaseApplication.onNetError 统一处理
    // ViewModel 可以覆盖该方法，实现 ViewModel 单独的 catch 处理，不会影响其他 ViewModel
    // 异常相关处理，对应 BaseThrowable.ExternalThrowable
    // errorType = 1表示请求出错如404，2表示代码出现bug
    open fun catchErr(e: Throwable, errorType: Int) {
        refreshing.postValue(false)
        moreLoading.postValue(false)
        e.printStackTrace()
        val e1 = BaseThrowable.ExternalThrowable(e)
        error.postValue(e1)
    }

    /**
     * 较少用到，单独处理字符串返回的情况
     */
    fun requestStringData(
        flow: Flow<String>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
        parseBolck: (String) -> Unit
    ) {
        viewModelScope.launch {
            parseRequest(flow, showLoading, loadingTips, ignoreCacheErr).collect {
                dowithTry(catchBlock = {
                    if (!ignoreCacheErr) catchErr(it, 2)
                }, {
                    parseBolck(it)
                })
            }
        }
    }

    /**
     * 普通方式请求普通接口，不使用 livedata
     */
    fun <Y, T : IResponse<Y>> requestData(
        flow: Flow<T>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
        ignoreResponseFilter: Boolean = false,
        parseBolck: (T) -> Unit
    ) {
        viewModelScope.launch {
            parseRequest(flow, showLoading, loadingTips, ignoreCacheErr).collect {
                dowithTry(catchBlock = {
                    if (!ignoreCacheErr) catchErr(it, 2)
                }, {
                    if (ignoreResponseFilter || responseFilter(it)) {
                        parseBolck(it)
                    }
                })
            }
        }
    }

    /**
     * 普通方式请求列表接口，不使用 livedata
     */
    fun <R, Y, T : IResponse<Y>> requestListData(
        flow: Flow<T>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
        ignoreResponseFilter: Boolean = false,
        parseBolck: (pageBean: BasePageBean<Y?>) -> Unit
    ) {
        viewModelScope.launch {
            parseRequest(flow, showLoading, loadingTips, ignoreCacheErr).collect {
                refreshing.postValue(false)
                moreLoading.postValue(false)
                dowithTry(catchBlock = {
                    if (!ignoreCacheErr) catchErr(it, 2)
                }, {
                    val pageBean = BasePageBean(it.resultData, page.value!!)
                    hasMore.postValue(pageBean.hasMoreData)
                    if (ignoreResponseFilter || responseFilter(it)) {
                        parseBolck(pageBean)
                    }
                })
            }
        }
    }

    /**
     * 普通接口请求，只返回业务数据
     * @param reqBolck 请求网络获取数据的方法体
     * @param watchTag 监听该字段，自动请求接口
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语（传 null 显示默认提示语，传 其他值则显示对应的值，传 空字符串 显示也为空字符串）
     * @param ignoreCacheErr 忽略公用的异常捕捉器
     * @param ignoreResponseFilter 忽略公用响应的过滤器
     * @param liveDataNotNull 返回的 LiveData 不为空，true 表示 LiveData 肯定会有值 为 null 则不会发送 livedata
     * @param parseBolck 处理数据的方法体，该方法的返回值将作为 LiveData 对外提供
     *     所有请求的执行顺序：reqBolck(请求体) -> parseRequest(请求统一处理) -> responseFilter(响应统一过滤) -> parseBolck(响应具体处理) -> catchErr(异常处理)
     *
     * 泛型说明
     *      R: 通过 parseBolck 方法将接口返回的原始类型 Y 处理为 R 类型，Y 和 R 可以是同一类型，可以是不同类型
     *      Y: 实际的业务数据，去掉 IResponse 后，原始接口返回的类型
     *      T: IResponse<R>，接口返回的包裹业务数据的
     *      W: 请求监听的对象
     */
    fun <R, Y, T : IResponse<Y>, W> requestDataToLiveData(
        reqBolck: (W) -> Flow<T>,
        watchTag: UnPeekLiveData<W>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
        ignoreResponseFilter: Boolean = false,
        liveDataNotNull: Boolean = false,
        parseBolck: (Y?) -> R
    ): LiveData<R?> {
        return map(
            liveDataNotNull,
            Transformations.switchMap(watchTag) {
                parseRequest(reqBolck(it), showLoading, loadingTips, ignoreCacheErr).asLiveData()
            }
        ) {
            dowithTry(catchBlock = {
                if (!ignoreCacheErr) catchErr(it, 2)
            }, {
                if (ignoreResponseFilter || responseFilter(it)) {
                    return@map parseBolck(it?.resultData)
                } else {
                    return@map null
                }
            })
            null
        }
    }

    /**
     * 列表页面请求，监听 page 自动请求列表接口，只返回业务数据
     * @param reqBolck 网络请求的方法体，参数为 当前的分页页码
     * @param showLoading 显示加载中的 loading
     * @param loadingTips 加载中的提示语
     * @param ignoreCacheErr 忽略错误的 try，包括 网络错误，业务错误，代码错误，三种错误的捕获
     * @param ignoreResponseFilter 忽略对响应的过滤，为 true 不执行 responseFilter 方法，为 false 则执行，默认 false
     * @param mPage 分页加载的页标，默认使用BaseViewModel 中的 page ，防止多个接口共用一个page出问题，每个接口可以指定单独的page，需要和BaseListFragment中的 onLoadMore / onRefresh 方法结合使用
     * @param mRefreshing 同上 mPage
     * @param mMoreLoading 同上
     * @param mHasMore 同上
     * @param parseBolck 处理响应的数据
     */
    fun <R, Y, T : IResponse<Y>> requestListDataToLiveData(
        reqBolck: (Int) -> Flow<T>,
        showLoading: Boolean = Settings.Request.showLoading,
        loadingTips: String? = null,
        ignoreCacheErr: Boolean = false,
        ignoreResponseFilter: Boolean = false,
        liveDataNotNull: Boolean = false,
        mPage: UnPeekLiveData<Int> = page,
        mRefreshing: UnPeekLiveData<Boolean> = refreshing,
        mMoreLoading: UnPeekLiveData<Boolean> = moreLoading,
        mHasMore: UnPeekLiveData<Boolean> = hasMore,
        parseBolck: (pageBean: BasePageBean<Y?>) -> R
    ): LiveData<R?> {
        return map(
            liveDataNotNull,
            Transformations.switchMap(mPage) {
                parseRequest(reqBolck(it), showLoading, loadingTips, ignoreCacheErr).asLiveData()
            }
        ) {
            mRefreshing.postValue(false)
            mMoreLoading.postValue(false)
            dowithTry(catchBlock = {
                if (!ignoreCacheErr) catchErr(it, 2)
            }, {
                val pageBean = BasePageBean(it?.resultData, mPage.value!!)
                mHasMore.postValue(pageBean.hasMoreData)
                if (ignoreResponseFilter || responseFilter(it)) {
                    val result = parseBolck(pageBean)
                    return@map result
                } else {
                    return@map null
                }
            })
            null
        }
    }

    @MainThread
    open fun <Y, R> map(
        liveDataNotNull: Boolean,
        source: LiveData<Y>,
        mapFunction: Function<Y?, R>
    ): LiveData<R> {
        val result = if (liveDataNotNull) NonNullMutableLiveData<R>() else MediatorLiveData<R>()
        result.addSource(
            source
        ) { x -> result.value = mapFunction.apply(x) }
        return result
    }
}