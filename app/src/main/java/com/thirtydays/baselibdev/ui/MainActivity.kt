package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.common.LanguageHelper
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.click.MainClick
import com.thirtydays.baselibdev.databinding.ActivityMainBinding
import com.thirtydays.baselibdev.router.RouterPath
import com.thirtydays.baselibdev.router.startDuLi
import com.thirtydays.baselibdev.router.startFragment
import com.thirtydays.baselibdev.vm.MainViewModel
import com.thirtydays.baselibdev.vm.TwoViewModel


@Route(path = RouterPath.AppCenter.PATH_APP_MAIN)
class MainActivity : BaseVmActivity<ActivityMainBinding>(R.layout.activity_main), MainClick {


    @VMScope
    lateinit var twoViewModel: TwoViewModel
    @VMScope lateinit var mainViewModel: MainViewModel

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mainViewModel
        mDataBinding.click = this
    }

    override fun initData() {
        mainViewModel.getTime()
        twoViewModel.getTime()
    }

    override fun openTestPage() {
        ARouter.getInstance().build("/test/test").navigation()
    }

    override fun openDuLiFragment() {
        startDuLi()
    }

    override fun openSwitchLangPage() {
        ARouter.getInstance().build("/change/lang").navigation()
    }

    override fun openChangeThemePage() {
        ARouter.getInstance().build("/change/theme").navigation()
    }

    override fun openFontPage() {
        ARouter.getInstance().build("/test/font").navigation()
    }

    override fun changeLang(lang:String) {
        LanguageHelper.switchLanguage(lang)
    }

    override fun openList() {
        startFragment("/test/list")
    }
}