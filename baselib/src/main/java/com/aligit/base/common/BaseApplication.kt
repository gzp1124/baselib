package com.aligit.base.common

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDexApplication
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.BuildConfig
import com.aligit.base.Settings
import com.aligit.base.Settings.app_force_use_portrait
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.initWebViewDataDirectory
import com.aligit.base.ext.tool.isLandscape
import com.aligit.base.net.ok.OkHttpManager
import com.aligit.base.net.retrofit.RetrofitFactory
import com.aligit.base.widget.loadpage.CustomLoadMoreView
import com.chad.library.adapter.base.module.LoadMoreModuleConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig
import okhttp3.OkHttpClient

/**
 * author : gzp1124
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

    /**
     * 更新全局的 Settings 文件
     */
    abstract fun updateSettings()

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        val rootDir: String = MMKV.initialize(this)

        // 更新全局的 Settings 文件
        updateSettings()

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


        //ARouter初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug()
        }
        ARouter.init(this)

        // 设置屏幕适配参数
        setAutoSizeConfig()

        // 滑动返回
        BGASwipeBackHelper.init(this,null)

        //腾讯
        CrashReport.initCrashReport(this, "3dd9e06f7e", BuildConfig.DEBUG)

        //多语言切换
        LanguageHelper.switchLanguage()

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        LoadMoreModuleConfig.defLoadMoreView = CustomLoadMoreView()

    }

    fun setAutoSizeConfig(){
        // 适配
        if (Settings.useAutoSize) {
            if (!app_force_use_portrait && isLandscape) {
                AutoSizeConfig.getInstance().designWidthInDp = Settings.app_landscape_screen_width.toInt()
                AutoSizeConfig.getInstance().designHeightInDp = Settings.app_landscape_screen_height.toInt()
            } else {
                AutoSizeConfig.getInstance().designWidthInDp = Settings.app_portrait_screen_width.toInt()
                AutoSizeConfig.getInstance().designHeightInDp = Settings.app_portrait_screen_height.toInt()
            }
            AutoSizeConfig.getInstance().isCustomFragment = true
            AutoSizeConfig.getInstance().isBaseOnWidth = Settings.autoSizeIsBaseOnWidth
            AutoSize.initCompatMultiProcess(this)
        }
    }

}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}