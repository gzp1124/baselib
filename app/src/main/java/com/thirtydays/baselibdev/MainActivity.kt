package com.thirtydays.baselibdev

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.common.LanguageHelper
import com.aligit.base.common.LanguageStatus
import com.aligit.base.ext.startWebFragment
import com.aligit.base.ext.tool.log
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.activity.BaseVmActivity
import com.aligit.base.utils.PermissionUtil
import com.thirtydays.baselibdev.adapter.MainAdapter
import com.thirtydays.baselibdev.click.MainClick
import com.thirtydays.baselibdev.databinding.ActivityMainBinding
import com.thirtydays.baselibdev.net.bean.MainBean
import com.thirtydays.baselibdev.router.RouterPath.AppCenter.PATH_APP_MAIN
import com.thirtydays.baselibdev.vm.MainViewModel
import com.thirtydays.baselibdev.vm.TestViewModel


@Route(path = PATH_APP_MAIN)
class MainActivity : BaseVmActivity<ActivityMainBinding>(R.layout.activity_main), MainClick {


    // 这里的 testViewModel 用于测试数据倒灌，不要删除
    @VMScope("TestErrorData")
    lateinit var testViewModel: TestViewModel

    //    @VMScope lateinit var twoViewModel: TwoViewModel
    @VMScope("MainActivity")
    lateinit var mViewModel: MainViewModel
    @VMScope
    lateinit var mainViewModel: MainViewModel
    val adapter = MainAdapter()


    val pages = arrayListOf(
        MainBean("测试MP折线图","/test/mp_chart",true),
        MainBean("测试圆形进度条", "/test/cicle_progress", true),
        MainBean("测试 SelectImage 图片选择器", "/test/select_image", true),
        MainBean("测试 databinding 转换器", "/test/test_convert", true),
        MainBean("测试 hide show 切换 fragment，请求网络", "/test/hide_show", true),
        MainBean("测试正则", "/test/reg", true),
        MainBean("测试阴影", "/test/shadow", true),
        MainBean("测试参数自动校验", "/test/auto_check_param", true),
        MainBean("打开test页面", "/test/test", false),
        MainBean("打开独立Fragment", "/test/duli", true),
        MainBean("打开切换夜间模式页面", "/change/lang", false),
        MainBean("打开切换主题页面", "/change/theme", false),
        MainBean("字体的使用", "/test/font", false),
        MainBean("列表示例", "/test/list", true),
        MainBean("列表示例 - 使用 BaseListFragment", "/test/list2", true),
        MainBean("测试数据倒灌", "/test/errordata", true),
        MainBean("测试各种请求网络", "/test/normal_request", true),
        MainBean("使用Flow代替LiveData", "/test/flow", true),
        MainBean("测试沉浸式问题", "/test/immersionBar", false),
        MainBean("测试定位获取经纬度", "/test/getlocation", false),
        MainBean("测试影片播放","/test/4kmovie",true),
        MainBean("测试 XUI 库的使用", "/test/test_xui", true)
    )

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mainViewModel
        mDataBinding.click = this
    }

    override fun initData() {
//        mainViewModel.getTime()
//        twoViewModel.getTime()
        mDataBinding.tt?.click{
            toast { "fdajklfdjsak" }
        }
        mDataBinding.recyclerView?.also {
            adapter.setList(pages)
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun changeLang(lang: LanguageStatus) {
        LanguageHelper.switchLanguage(lang)
    }
}