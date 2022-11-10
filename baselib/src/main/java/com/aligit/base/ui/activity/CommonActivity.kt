package com.aligit.base.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.databinding.ActivityCommonBinding
import com.aligit.base.ext.view.setGone

interface CommonActivityEvent {
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
}

interface CommonActivityOnResult {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

class CommonActivity : BaseVmActivity<ActivityCommonBinding>(R.layout.activity_common) {

    var fragmentPath: String? = ""
    var fragmentBundle: Bundle? = null

    private var showToolbar:Boolean = false
    private var toolbarTitle:String = ""
    private var toolbarTitleCenter:Boolean = false

    override fun onInitDataBinding() {

        showToolbar = intent.getBooleanExtra("showToolbar",false)
        toolbarTitle = intent.getStringExtra("toolbarTitle") ?: ""
        toolbarTitleCenter = intent.getBooleanExtra("toolbarTitleCenter",false)

        mDataBinding.run {
            toolbar.setGone(!showToolbar)
            toolbar.setNavigationOnClickListener { finish() }
            centerTitle.text = toolbarTitle
            leftTitle.text = toolbarTitle
            centerTitle.setGone(!toolbarTitleCenter)
            leftTitle.setGone(toolbarTitleCenter)
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return intent.getBooleanExtra("useSwipeBack", Settings.UI.useSwipeBack)
    }

    // 这里调用太早，会导致 intent 空指针
//    override fun autoSizeBaseWidth(): Boolean {
//        return intent.getBooleanExtra("autoSizeIsBaseOnWidth",Settings.AutoSize.autoSizeIsBaseOnWidth)
//    }

    override fun booHideBottom(): Boolean {
        return intent.getBooleanExtra("isHideBottom", Settings.UI.hasNavigationBar)
    }

    override fun isImmersionBar(): Boolean {
        return intent.getBooleanExtra("useImmersionBar", Settings.UI.useImmersionBar)
    }

    private lateinit var fragment: Fragment

    override fun initData() {
        try {
            fragmentBundle = intent.getBundleExtra("fragmentBundle")
            fragmentPath = intent.getStringExtra("fragmentPath")
            fragment = ARouter.getInstance().build(fragmentPath).navigation() as Fragment
            fragmentBundle?.let { fragment.arguments = it }
            supportFragmentManager.beginTransaction().replace(R.id.commonFrameLin, fragment)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (fragment is CommonActivityEvent) {
            val res = (fragment as CommonActivityEvent).onKeyDown(keyCode, event)
            if (res) return res
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fragment is CommonActivityOnResult) {
            (fragment as CommonActivityOnResult).onActivityResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}