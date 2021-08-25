package com.aligit.base.ui.foundation.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.foundation.onError
import com.aligit.base.ext.tool.screenHeight
import com.aligit.base.ext.tool.screenWidth
import com.aligit.base.ext.tool.toast
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.model.CoroutineState
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.permissionx.guolindev.PermissionX
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2021/7/30
 * @description : toast、dialog、error
 * </pre>
 */
abstract class BaseActivity : InternationalizationActivity() {

    protected open val mMainHandler = Handler(Looper.getMainLooper())

    protected open lateinit var mPermission: com.permissionx.guolindev.PermissionCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter注册
        ARouter.getInstance().inject(this)

        mPermission = PermissionX.init(this)

        /*if (isFullScreen){
            AutoSizeConfig.getInstance().onAdaptListener = object : onAdaptListener {
                override fun onAdaptBefore(target: Any, activity: Activity) {
                    //ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                    AutoSizeConfig.getInstance().screenWidth =
                        ScreenUtils.getScreenSize(activity).get(0)
                    AutoSizeConfig.getInstance().screenHeight =
                        ScreenUtils.getScreenSize(activity).get(1)
                }

                override fun onAdaptAfter(target: Any, activity: Activity) {}
            }
        }*/
        AutoSizeConfig.getInstance().screenHeight = screenHeight
        AutoSizeConfig.getInstance().screenWidth = screenWidth
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AutoSizeConfig.getInstance().screenHeight = screenHeight
        AutoSizeConfig.getInstance().screenWidth = screenWidth
    }

    //共用 loading View
    protected val loadingView: BasePopupView by lazy {
        initLoadingView()
    }

    open fun initLoadingView(): BasePopupView {
        //初始加载框
        return XPopup.Builder(this).asLoading(getString(R.string.loading))
    }

    open fun showLoading(tip: String? = getString(R.string.loading)) {
        if (!loadingView.isShow) {
            (loadingView as? LoadingPopupView)?.setTitle(tip)
            loadingView.show()
        }
    }

    open fun hideLoading() {
//        if (loadingView.isShow) loadingView.dismiss()
    }

    open fun onError(throwable: BaseThrowable) {
        hideLoading()
        throwable.onError()
    }

    open fun showToast(msg: String) {
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        runOnUiThread { toast { msg } }
    }

    override fun getResources(): Resources {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            var rw: Float
            var rh: Float
            if (super.getResources().configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rw = Settings.app_landscape_screen_width
                rh = Settings.app_landscape_screen_height
            } else {
                rw = Settings.app_portrait_screen_width
                rh = Settings.app_portrait_screen_height
            }
            AutoSizeConfig.getInstance().designWidthInDp = rw.toInt()
            AutoSizeConfig.getInstance().designHeightInDp = rh.toInt()
            AutoSizeCompat.autoConvertDensity(super.getResources(), rw, true)
        }
        return super.getResources()
    }

    fun initViewModelActions(mViewModel: BaseViewModel) {
        mViewModel.run {
            observe(error) {
                it.onError()
            }
            observe(statusLiveData) {
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
}