package com.aligit.base.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.R
import com.aligit.base.Settings
import com.aligit.base.databinding.ActivityCommonBinding

class CommonActivity:BaseVmActivity<ActivityCommonBinding>(R.layout.activity_common) {

    var fragmentPath:String? = ""
    var fragmentBundle:Bundle? = null

    override fun onInitDataBinding() {
    }

    override fun isSupportSwipeBack(): Boolean {
        return intent.getBooleanExtra("useSwipeBack",Settings.UI.useSwipeBack)
    }

    // 这里调用太早，会导致 intent 空指针
//    override fun autoSizeBaseWidth(): Boolean {
//        return intent.getBooleanExtra("autoSizeIsBaseOnWidth",Settings.AutoSize.autoSizeIsBaseOnWidth)
//    }

    override fun booHideBottom(): Boolean {
        return intent.getBooleanExtra("isHideBottom",Settings.UI.hasNavigationBar)
    }

    override fun isImmersionBar(): Boolean {
        return intent.getBooleanExtra("useImmersionBar",Settings.UI.useImmersionBar)
    }

    override fun initData() {
        try {
            fragmentBundle = intent.getBundleExtra("fragmentBundle")
            fragmentPath = intent.getStringExtra("fragmentPath")
            val fragment = ARouter.getInstance().build(fragmentPath).navigation() as Fragment
            fragmentBundle?.let { fragment.arguments = it }
            supportFragmentManager.beginTransaction().replace(R.id.commonFrameLin,fragment).commitAllowingStateLoss()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}