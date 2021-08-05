package com.seabreeze.robot.base.common

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDexApplication
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.module.LoadMoreModuleConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.seabreeze.robot.base.BuildConfig
import com.seabreeze.robot.base.Settings
import com.seabreeze.robot.base.Settings.app_force_use_portrait
import com.seabreeze.robot.base.Settings.language_status
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.initWebViewDataDirectory
import com.seabreeze.robot.base.ext.tool.isLandscape
import com.seabreeze.robot.base.net.RetrofitFactory
import com.seabreeze.robot.base.net.ok.OkHttpManager
import com.seabreeze.robot.base.widget.loadpage.CustomLoadMoreView
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import okhttp3.OkHttpClient

/**
 * User: milan
 * Time: 2020/4/8 9:40
 * Des:
 */
private lateinit var INSTANCE: Application

abstract class BaseApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)

        initWebViewDataDirectory()
    }

    /**
     * 网络错误的统一封装
     */
    abstract fun onNetError(err: BaseThrowable)

    companion object {

        // 深色模式
        val darkMode: MutableLiveData<Int> by lazy{
            MutableLiveData(Settings.dark_model)
        }

        val okHttpClient: OkHttpClient by lazy {
            OkHttpManager.INSTANCE.initOkHttpClient(INSTANCE)
        }

        val retrofitFactory: RetrofitFactory by lazy { RetrofitFactory.getInstance(okHttpClient) }
    }

    private val TAG = "Base_gzp1124"

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        //XLog
        val config = LogConfiguration.Builder()
            .tag(TAG)
            .build()
        XLog.init(config, AndroidPrinter())

        //Logger
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag(TAG)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        val rootDir: String = MMKV.initialize(this)
        XLog.i(rootDir)

        //ARouter初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug()
        }
        ARouter.init(this)

        // 适配
        if (!app_force_use_portrait && isLandscape) {
            AutoSizeConfig.getInstance().designWidthInDp = Settings.app_landscape_screen_width.toInt()
            AutoSizeConfig.getInstance().designHeightInDp = Settings.app_landscape_screen_height.toInt()
        }else{
            AutoSizeConfig.getInstance().designWidthInDp = Settings.app_portrait_screen_width.toInt()
            AutoSizeConfig.getInstance().designHeightInDp = Settings.app_portrait_screen_height.toInt()
        }
        AutoSizeConfig.getInstance().isCustomFragment = true
        AutoSize.initCompatMultiProcess(this)

        // 滑动返回
        BGASwipeBackHelper.init(this,null)

        //腾讯
        CrashReport.initCrashReport(this, "3dd9e06f7e", BuildConfig.DEBUG)

        language_status.let {
            LanguageHelper.setLanguage(this, it,isForce = true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        LoadMoreModuleConfig.defLoadMoreView = CustomLoadMoreView()
    }

}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}