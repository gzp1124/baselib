package com.thirtydays.baselib

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import com.thirtydays.baselib.BaseApplication.Companion.INSTANCE
import com.thirtydays.baselib.common.LanguageHelper
import com.thirtydays.baselib.net.DataManager
import com.thirtydays.baselib.net.ok.OkHttpManager
import okhttp3.OkHttpClient

/**
 * User: milan
 * Time: 2020/4/8 9:40
 * Des:
 */

open class BaseApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var INSTANCE: Application

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpManager.INSTANCE.initOkHttpClient(INSTANCE)
        }
        val dataManager: DataManager by lazy { DataManager.getInstance(okHttpClient) }
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        val rootDir: String = MMKV.initialize(this)

        //ARouter初始化
        ARouter.openLog() // 打印日志
        ARouter.openDebug()
        ARouter.init(this)

        //腾讯
//        CrashReport.initCrashReport(this, "d40d0616af", false)
//        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG)

        Settings.language_status.let {
            LanguageHelper.switchLanguage(this, it, isForce = true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
//        LoadMoreModuleConfig.defLoadMoreView = CustomLoadMoreView()
    }

}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}