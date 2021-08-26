package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.common.LanguageHelper
import com.aligit.base.common.LanguageStatus
import com.aligit.base.ext.foundation.Mmkv
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.click.MainClick
import com.thirtydays.baselibdev.databinding.ActivityMainBinding
import com.thirtydays.baselibdev.router.RouterPath
import com.thirtydays.baselibdev.router.startDuLi
import com.thirtydays.baselibdev.router.startFragment
import com.thirtydays.baselibdev.vm.MainViewModel
import com.thirtydays.baselibdev.vm.TestViewModel
import com.thirtydays.baselibdev.vm.TwoViewModel


@Route(path = RouterPath.AppCenter.PATH_APP_MAIN)
class MainActivity : BaseVmActivity<ActivityMainBinding>(R.layout.activity_main), MainClick {


    @VMScope("TestErrorData") lateinit var testViewModel: TestViewModel

    @VMScope
    lateinit var twoViewModel: TwoViewModel
    @VMScope lateinit var mainViewModel: MainViewModel

    companion object{
        var test by Mmkv("save",B(),B::class.java)
    }

    class B {
        var s:String = "123"
        var ll:MutableList<String> = arrayListOf()
    }

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mainViewModel
        mDataBinding.click = this
    }

    override fun initData() {
//        mainViewModel.getTime()
        twoViewModel.getTime()
    }

    val b = B()
    override fun onResume() {
        super.onResume()
        b.s = "从这里设置的"
        b.ll.add("11")
        test = b
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

    override fun changeLang(lang: LanguageStatus) {
        LanguageHelper.switchLanguage(lang)
    }

    override fun openList() {
        startFragment("/test/list")
    }

    //测试数据倒灌
    override fun testErrorData() {
        startFragment("/test/errordata")
    }
}