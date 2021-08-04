package com.seabreeze.robot.base.ui.foundation.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.launcher.ARouter
import com.permissionx.guolindev.PermissionCollection
import com.permissionx.guolindev.PermissionX
import com.seabreeze.robot.base.Settings
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.ext.tool.screenHeight
import com.seabreeze.robot.base.ext.tool.screenWidth
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.base.ui.fragment.ProgressDialogFragment
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * @description : toast、dialog、error
 * </pre>
 */
abstract class BaseActivity : InternationalizationActivity() {

    protected open val mMainHandler = Handler(Looper.getMainLooper())

    protected open lateinit var mPermission: PermissionCollection

    private lateinit var progressDialogFragment: ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutoSizeConfig.getInstance().screenHeight = screenHeight
        AutoSizeConfig.getInstance().screenWidth = screenWidth

        //ARouter注册
        ARouter.getInstance().inject(this)

        mPermission = PermissionX.init(this)
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AutoSizeConfig.getInstance().screenHeight = screenHeight
        AutoSizeConfig.getInstance().screenWidth = screenWidth
    }

    /**
     * 显示加载(转圈)对话框
     */
    open fun showProgressDialog(message: String? = null) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(supportFragmentManager, message, false)
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
}