package com.aligit.base.ui.foundation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.aligit.base.R
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.dowithTry
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.foundation.onError
import com.aligit.base.ext.tool.isDarkMode
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.tool.unregisterEvent
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.model.CoroutineState
import com.aligit.base.ui.foundation.activity.BaseActivity
import com.aligit.base.ui.foundation.activity.SwipeBackActivity
import com.blankj.utilcode.util.FragmentUtils
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX


/**
 * <pre>
 * author : gzp1124
 *
 * 基类 Fragment
 * 注意：
 * 所有的 Fragment 都不要使用带有参数的构造方法
 * 错误示例：class HomeworkListFragment(val index: Int) :BaseListFragment() {}
 * 如果要给 Fragment 中传递参数，可以使用 Bundle 进行传参
 *
 * SimpleImmersionOwner 的作用：https://github.com/gyf-dev/ImmersionBar/blob/master/immersionbar-components/src/main/java/com/gyf/immersionbar/components/SimpleImmersionFragment.java
 * 为了方便在Fragment使用沉浸式
 *
 * @version : 1.0
 * @date : 2021/7/30
</pre> *
 */
abstract class BaseFragment : LazyLoadFragment(), SimpleImmersionOwner {
    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)

    protected open val mMainHandler = Handler(Looper.getMainLooper())

    protected open lateinit var mPermission: PermissionMediator

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPermission = PermissionX.init(this)
    }

    override fun onDestroy() {
        hideLoading()
        unregisterEvent()
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when(event){
                    Lifecycle.Event.ON_DESTROY -> onActivityDestroy()
                    Lifecycle.Event.ON_RESUME -> onActivityResume()
                    Lifecycle.Event.ON_PAUSE -> onActivityPause()
                    else -> {}
                }
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    open fun onActivityResume(){}
    open fun onActivityPause(){}
    open fun onActivityDestroy(){}

    open fun showLoading(tip: String? = getString(R.string.loading)) {
        (activity as? BaseActivity)?.showLoading(tip)
    }

    open fun hideLoading() {
        (activity as? BaseActivity)?.hideLoading()
    }

    open fun onError(throwable: BaseThrowable) {
        hideLoading()
        throwable.onError()
    }

    open fun showToast(msg: String) {
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        mMainHandler.post { toast { msg } }
    }

    /**
     * 监听请求状态和错误响应
     */
    fun initViewModelActions(mViewModel: BaseViewModel) {
        mViewModel.run {
            observe(error){
                it.onError()
            }
            observe(statusLiveData){
                when (it) {
                    is CoroutineState.Loading -> {
                        showLoading(it.loadingTips)
                    }
                    is CoroutineState.Finish -> {
                        hideLoading()
                    }
                    is CoroutineState.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    var isShow = false
    override fun onResume() {
        super.onResume()
        isShow = true
    }

    override fun onPause() {
        super.onPause()
        isShow = false
    }

    fun showFragment(
        @IdRes containerId: Int,
        key: String,
        hideSameFrameId: Boolean = true,
        createFragment: (key: String) -> BaseFragment
    ) {
        FragmentUtils.getFragments(childFragmentManager)?.forEach { f ->
            if (hideSameFrameId) {
                if (f != null && f is BaseFragment && f.isShow) {
                    f.view?.parent?.let {
                        val cid = (it as ViewGroup).id
                        if (containerId == cid){
                            FragmentUtils.hide(f)
                        }
                    }
                }
            }else {
                if (f != null && f is BaseFragment && f.isShow) {
                    FragmentUtils.hide(f)
                }
            }
        }
        FragmentUtils.findFragment(childFragmentManager, key)?.let {
            FragmentUtils.show(it)
        } ?: let {
            FragmentUtils.add(
                childFragmentManager,
                createFragment(key),
                containerId,
                key
            )
        }
    }

    override fun initImmersionBar() {
        dowithTry {
            (activity as SwipeBackActivity).setImmersionBar()
        }
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return true
    }
}