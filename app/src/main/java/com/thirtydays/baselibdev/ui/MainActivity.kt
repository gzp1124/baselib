package com.thirtydays.baselibdev.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.ext.view.singleClick
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.NoViewModel
import com.seabreeze.robot.base.framework.mvvm.scope.VMScope
import com.seabreeze.robot.base.framework.mvvm.scope.injectViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import com.thirtydays.baselibdev.router.startMain
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityMainBinding
import com.thirtydays.baselibdev.net.bean.TestBean
import com.thirtydays.baselibdev.router.RouterPath
import com.thirtydays.baselibdev.vm.TwoViewModel

@Route(path = RouterPath.AppCenter.PATH_APP_MAIN)
class MainActivity : BaseVmActivity<NoViewModel,ActivityMainBinding>(R.layout.activity_main) {


    @VMScope("test")
    lateinit var twoViewModel: TwoViewModel

    override fun onInitDataBinding() {
        findViewById<View>(R.id.openPage).setOnClickListener {
            ARouter.getInstance().build("/test/test").navigation()
            finish()
        }
    }

    override fun initData() {
        injectViewModel()
        twoViewModel.getTime()
    }
}