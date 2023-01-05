package com.aligit.base.ui.foundation.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.foundation.onError
import com.aligit.base.ext.tool.isDarkMode
import com.aligit.base.ext.tool.isLandscape
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.tool.unregisterEvent
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.model.CoroutineState
import com.aligit.base.ui.foundation.fragment.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX
import me.jessyan.autosize.AutoSizeCompat

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

    protected open lateinit var mPermission: PermissionMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        updateAutoSizeBaseWidth()
        super.onCreate(savedInstanceState)
        //ARouter注册
        ARouter.getInstance().inject(this)

        mPermission = PermissionX.init(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //注释掉 super 的调用，意味着不保存任何信息，
        // 如果 activity 被回收需要重建，表现出来的形式就是 activity 重新打开了
//        super.onSaveInstanceState(outState)

        //单独使用下面的一行，只是不保存 fragment 的信息，也会发生 fragment 的重叠
        // 不保存 fragment 的任何信息，防止 fragment 重叠
//        outState.putParcelable("android:support:fragments", null)
    }

    override fun onDestroy() {
        hideLoading()
        unregisterEvent()
        super.onDestroy()
    }

    /**
     * 指定 autoSize 使用的布局宽度
     * autoSizeBaseWidth 为 true 时以 该值 为基准进行适配
     */
    open fun autoSizeWidth(): Float {
        val w = if (isLandscape) {
            Settings.AutoSize.app_landscape_screen_width
        } else {
            Settings.AutoSize.app_portrait_screen_width
        }
        return w
    }

    /**
     * 指定 autoSize 使用的布局高度
     * autoSizeBaseWidth 为 false 时以 该值 为基准进行适配
     */
    open fun autoSizeHeight(): Float {
        val h = if (isLandscape) {
            Settings.AutoSize.app_landscape_screen_height
        } else {
            Settings.AutoSize.app_portrait_screen_height
        }
        return h
    }

    /**
     * autosize 是否使用宽度为基准进行适配
     * 返回 true 表示宽度为 autoSizeWidth 指定的宽度，高度进行等比例计算
     * 返回 false 则相反
     */
    open fun autoSizeBaseWidth(): Boolean = Settings.AutoSize.autoSizeIsBaseOnWidth

    /**
     * 更新布局适配的基本 宽/高 autosize 将进行自动适配
     */
    open fun updateAutoSizeBaseWidth() {
        if (Settings.AutoSize.useAutoSize && Looper.myLooper() == Looper.getMainLooper()) {
            if (autoSizeBaseWidth()) {
                AutoSizeCompat.autoConvertDensity(super.getResources(), autoSizeWidth(), true)
            } else {
                AutoSizeCompat.autoConvertDensity(super.getResources(), autoSizeHeight(), false)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateAutoSizeBaseWidth()
    }

    //共用 loading View
    protected val loadingView: BasePopupView by lazy {
        initLoadingView()
    }

    open fun initLoadingView(): BasePopupView {
        //初始加载框
        return XPopup.Builder(this)
            .hasNavigationBar(Settings.UI.hasNavigationBar)
            .hasStatusBarShadow(false)
            .isLightStatusBar(!isDarkMode())
            .hasStatusBar(Settings.UI.hasStatusBar)
            .asLoading(getString(R.string.loading))
    }

    open fun showLoading(tip: String? = getString(R.string.loading)) {
        if (!loadingView.isShow) {
            (loadingView as? LoadingPopupView)?.setTitle(tip ?: getString(R.string.loading))
            loadingView.show()
        }
    }

    open fun hideLoading() {
        loadingView.smartDismiss()
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
        updateAutoSizeBaseWidth()
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

    /*
    给 activity 展示 fragment，一个 activity 中只能展示一个 fragment 
    要展示多个 fragment ，就不能用这个方法
    一般用于首页切换 tab ，会根据 key 自动显示已存在的 fragment ，同时隐藏其他的 fragment
    如果根据 key 获取不到 fragment ，则会调用 createFragment 创建一个新的 fragment
     */
    fun showFragment(
        @IdRes containerId: Int,
        key: String,
        hideSameFrameId: Boolean = true,
        createFragment: (key: String) -> BaseFragment
    ) {
        FragmentUtils.getFragments(supportFragmentManager)?.forEach { f ->
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
        FragmentUtils.findFragment(supportFragmentManager, key)?.let {
            FragmentUtils.show(it)
        } ?: let {
            FragmentUtils.add(
                supportFragmentManager,
                createFragment(key),
                containerId,
                key
            )
        }
    }

    //记录用户首次点击返回键的时间
    private var firstTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (doubleClickFinish() && keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            val secondTime = System.currentTimeMillis()
            if (secondTime - firstTime > doubleClickFinishTimeM()) {
                showToast(doubleClickFinishNotifyStr())
                firstTime = secondTime
            } else {
                ActivityUtils.finishAllActivities()
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 启用双击退出应用
     */
    open fun doubleClickFinish() = false

    /**
     * 双击退出应用提示语
     */
    open fun doubleClickFinishNotifyStr() = "再按一次退出应用"

    /**
     * 双击退出应用的两次点击时间最大间隔
     */
    open fun doubleClickFinishTimeM() = 2000
}