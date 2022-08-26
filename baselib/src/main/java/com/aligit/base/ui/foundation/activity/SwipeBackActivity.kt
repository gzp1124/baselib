package com.aligit.base.ui.foundation.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.Settings.UI.app_force_use_portrait
import com.aligit.base.common.AppManager
import com.aligit.base.common.BaseApplication
import com.aligit.base.ext.find
import com.aligit.base.ext.tool.isDarkMode
import com.aligit.base.ext.tool.setScreenLandscape
import com.aligit.base.ext.tool.setScreenPortrait
import com.blankj.utilcode.util.BarUtils
import com.gyf.immersionbar.ImmersionBar

/**
 * author : gzp1124
 * Time: 2020/4/8 10:01
 * Des: 滑动返回、横竖屏、UI
 */
abstract class SwipeBackActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {

    private lateinit var mSwipeBackHelper: BGASwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //根类记录 Activity
        AppManager.addActivity(this)
        //横竖屏
        if (app_force_use_portrait == 1)setScreenPortrait()
        if (app_force_use_portrait == 2)setScreenLandscape()
        //滑动返回
        initSwipeBackFinish()
        //屏幕设置
        initImmersionUi()
    }

    override fun onDestroy() {
        AppManager.finishActivity(this)
        super.onDestroy()
    }

    //获取Window中视图content
    val contentView: View
        get() {
            val content = find<FrameLayout>(android.R.id.content)
            return content.getChildAt(0)
        }

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding) {
            return
        }
        mSwipeBackHelper.backward()
    }

    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
    }

    private fun initImmersionUi() {
        if (booHideBottom()) {
            //隐藏虚拟按键，并且全屏
            val window = window
            val params = window.attributes
            params.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            window.attributes = params
        }

        //status Bar 沉浸式状态栏相关设置
        BaseApplication.darkMode.observe(this) {
            setImmersionBar()
        }
    }

    val darkMode: MutableLiveData<Int> = MutableLiveData(Settings.UI.dark_model)

    // 隐藏虚拟按键
    protected open fun booHideBottom():Boolean = false
    // 使用沉浸式
    protected open fun isImmersionBar():Boolean = false

    open fun setImmersionBar() {
        val isDark = isDarkMode()
        val barColor = if (isDark)android.R.color.background_dark else android.R.color.background_light
        ImmersionBar.with(this)
            .keyboardEnable(true)
//            .titleBarMarginTop(R.id.toolbar)
            .fitsSystemWindows(!isImmersionBar(),barColor)
            .statusBarDarkFont(!isDark)
            .navigationBarColor(barColor) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(!isDark) //导航栏图标是深色，不写默认为亮色
//            .fullScreen(booHideBottom())
            .init()
    }

    protected open fun setStatusBar(view: View) {
        val statusHeight = BarUtils.getStatusBarHeight()
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            statusHeight
        )
    }

    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {
    }

    override fun onSwipeBackLayoutCancel() {
    }

    // 是否需要侧滑返回
    override fun isSupportSwipeBack():Boolean = Settings.UI.useSwipeBack

}
