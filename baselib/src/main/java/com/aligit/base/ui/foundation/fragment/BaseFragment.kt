package com.aligit.base.ui.foundation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.aligit.base.R
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.foundation.onError
import com.aligit.base.ext.tool.toast
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.model.CoroutineState
import com.aligit.base.ui.fragment.ProgressDialogFragment
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.permissionx.guolindev.PermissionCollection
import com.permissionx.guolindev.PermissionX

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2021/7/30
</pre> *
 */
abstract class BaseFragment : LazyLoadFragment(), SimpleImmersionOwner {

    protected open val mMainHandler = Handler(Looper.getMainLooper())

    protected open lateinit var mPermission: PermissionCollection

    private lateinit var progressDialogFragment: ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPermission = PermissionX.init(this)
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when(event){
                    Lifecycle.Event.ON_DESTROY -> onActivityDestroy()
                    Lifecycle.Event.ON_RESUME -> onActivityResume()
                    Lifecycle.Event.ON_PAUSE -> onActivityPause()
                }
            }
        })
    }

    open fun onActivityResume(){}
    open fun onActivityPause(){}
    open fun onActivityDestroy(){}

    /**
     * 显示加载(转圈)对话框
     */
    open fun showProgressDialog(message: String? = null) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(childFragmentManager, message, false)
        }
    }

    /**
     * 隐藏加载(转圈)对话框
     */
    open fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }

    open fun onError(throwable: BaseThrowable) {
        dismissProgressDialog()
        throwable.onError()
    }

    open fun showToast(msg: String) {
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        mMainHandler.post { toast { msg } }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
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
                        showProgressDialog(statusInfoStr)
                    }
                    is CoroutineState.Finish -> {
                        dismissProgressDialog()
                    }
                    is CoroutineState.Error -> {
                        dismissProgressDialog()
                    }
                }
            }
        }
    }
}