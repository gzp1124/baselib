package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.seabreeze.robot.base.BuildConfig
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.databinding.ActivityCommonBinding
import com.seabreeze.robot.base.ext.tool.toast

@Route(path = "/common/common")
class CommonActivity:BaseVmActivity<ActivityCommonBinding>(R.layout.activity_common) {

    @Autowired @JvmField var fragmentPath:String? = ""
    @Autowired @JvmField var fragmentBundle:Bundle? = null

    override fun onInitDataBinding() {

    }

    override fun initData() {
        try {
            val fragment = ARouter.getInstance().build(fragmentPath).navigation() as Fragment
            fragmentBundle?.let { fragment.arguments = it }
            supportFragmentManager.beginTransaction().replace(R.id.frameLin,fragment).commitAllowingStateLoss()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}