package com.aligit.base

import androidx.appcompat.app.AppCompatDelegate
import com.aligit.base.common.image.GlideEngine
import com.aligit.base.common.image.ImageLoderEngine
import com.aligit.base.ext.foundation.Mmkv
import java.util.*

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/28
 * desc   : 全局配置
 * </pre>
 */
object Settings {
    // debug
    var isDebug = true
    // 日志 Tag
    var logTag = "BaseLib"
    // 启用欢迎页授权隐私政策，不授权不能使用APP
    var useSplashCheckPrivacy = false
    // 是否每个版本都需要重新授权隐私政策
    var checkPrivacyForEachVersion = false

    object UI{
        // 强制使用竖屏/横屏 0: 不固定横竖屏，1:固定使用竖屏，2:固定使用横屏
        var app_force_use_portrait: Int by Mmkv("app_force_use_portrait",0)
        // 多语言，默认简体中文
        var language_status: Locale by Mmkv("LANGUAGE_STATUS", Locale.SIMPLIFIED_CHINESE, Locale::class.java)
        // 项目主题
        var project_theme: Int by Mmkv("PROJECT_THEME", R.style.BaseLibTheme)
        // 深色主题，1亮色，2深色，3跟随系统
        var dark_model: Int by Mmkv("PROJECT_DARK_MODEL", AppCompatDelegate.MODE_NIGHT_NO)
        // 使用滑动返回
        var useSwipeBack: Boolean by Mmkv("useSwipeBack", true)
        // 使用沉浸式
        var useImmersionBar: Boolean by Mmkv("useImmersionBar", false)
        // 顶部状态栏
        var hasStatusBar: Boolean by Mmkv("hasStatusBar", true)
        // 底部导航栏，虚拟按键
        var hasNavigationBar: Boolean by Mmkv("hasNavigationBar", true)
    }

    // autoSize 相关
    object AutoSize{
        // 使用AutoSize进行布局适配
        var useAutoSize: Boolean = true

        // autosize 使用宽度进行适配
        var autoSizeIsBaseOnWidth: Boolean = true

        // 配合 AutoSize 适配的参数，设计图的尺寸，主要是要宽高比
        var app_landscape_screen_width: Float = 1194f
        var app_landscape_screen_height: Float = 834f

        // 如果强制使用竖屏，只需要设置下面两个竖屏的宽高即可，设计图的尺寸
        var app_portrait_screen_width: Float = 375f
        var app_portrait_screen_height: Float = 812f
    }

    // 网络请求相关
    object Request{
        // 分页起始页页码
        var pageStartIndex = 1
        // 显示加载中的loading
        var showLoading = false
        // 请求url
        var BASE_URL: String = "http://101.200.207.61:9650/"
        // 请求网络的token key
        var appTokenHeadKey: String = "accessToken"
        // 登录后保存的用户标识
        var app_token: String by Mmkv("app_token_save", "")
        // token 异常的状态码 (即 app_token 失效)
        var tokenErrCode : String? = null
        // 保存 cookie
        var saveCookie: Boolean = false
        // 使用 okhttp 缓存
        var useCache: Boolean = false
        // okhttp 的最大缓存
        var cacheSize: Long = 1024 * 1024 * 100

        var connectTimeout: Long = 5L   // 连接超时事件，单位秒
        var readTimeout: Long = 5L      // 读取超时时间，单位秒
        var writeTimeout: Long = 5L     // 写入超时时间，单位秒
    }

    // 文件保存路径
    object fileSavePath {
        var rootPath = ""
        // HTTP 请求的缓存路径
        var httpCachePath = "http_cache"
    }

    object Tools{
        // 图片加载
        val imageLoader: ImageLoderEngine = GlideEngine.createGlideEngine()
    }

    // oss用到的常量
    val ossConstant: OssConstant = OssConstant()
    data class OssConstant(
        var BUCKET_NAME: String = "test",
        var ENDPOINT: String = "oss-cn-shenzhen.aliyuncs.com",
        var ACCESS_KEY_ID: String = "STS.NTcPUS54EThDAsymDYAyeY1mQ",
        var ACCESS_KEY_SECRET: String = "5i4eG4bXwgUoScJMMaZ9wu8qFB1oTLuXwN3E67TyGcJZ",
        var STS_TOKEN: String = "CAIS8wF1q6Ft5B2yfSjIr5fWG+/n2Otk46qvQ1XIiUQMTfZJtvTGszz2IHBJdHZtA+0atfkxlWpX7vcclqN3QplBRErLbdZxtlyoYoQ0Jdivgde8yJBZor/HcDHhJnyW9cvWZPqDP7G5U/yxalfCuzZuyL/hD1uLVECkNpv74vwOLK5gPG+CYCFBGc1dKyZ7tcYeLgGxD/u2NQPwiWeiZygB+CgE0DwmtvzvnJfAt0OB1gyhmtV4/dqhfsKWCOB3J4p6XtuP2+h7S7HMyiY46WIRpPom0/EepGuc44/MWQYJskucUe7d9NluKgB5e6kquSvoGXQhe5YagAFl6ZxdACLKhCCS4Wywjyo25L2qmZKfunawCNIj9oEmXex5f/Hte7SjENbN1UuKfjy7UDKkQA9UKSlmdcIJ/6IQlz5NTkupGBAq4/E+5PFd3duO1NiIJilTP2KNedkHD1cELBdqbxVbGj0Cxka3MTQHq7hLuIkOQoZKbc2UPVgIsw=="
    )
}