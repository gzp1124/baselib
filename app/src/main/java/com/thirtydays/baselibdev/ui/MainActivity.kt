package com.thirtydays.baselibdev.ui

import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.seabreeze.robot.base.ext.tool.screenHeight
import com.seabreeze.robot.base.ext.tool.screenWidth
import com.seabreeze.robot.base.ext.view.singleClick
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.scope.VMScope
import com.seabreeze.robot.base.framework.mvvm.scope.injectViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityMainBinding
import com.thirtydays.baselibdev.router.RouterPath
import com.thirtydays.baselibdev.vm.MainViewModel
import com.thirtydays.baselibdev.vm.TwoViewModel
import me.jessyan.autosize.AutoSizeConfig

@Route(path = RouterPath.AppCenter.PATH_APP_MAIN)
class MainActivity : BaseVmActivity<ActivityMainBinding>(R.layout.activity_main) {


    @VMScope lateinit var twoViewModel: TwoViewModel
    @VMScope lateinit var mainViewModel: MainViewModel

    override fun onInitDataBinding() {
        mDataBinding.dd = mainViewModel
    }

    override fun initData() {
        mainViewModel.getTime()
        twoViewModel.getTime()

        mDataBinding.openPage.singleClick {
            ARouter.getInstance().build("/test/test").navigation()
        }
    }
}