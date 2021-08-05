package com.seabreeze.robot.base

import androidx.appcompat.app.AppCompatDelegate
import com.seabreeze.robot.base.common.LanguageHelper.LANGUAGE_SYSTEM
import com.seabreeze.robot.base.ext.foundation.Mmkv

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object Settings {
    // 强制使用竖屏
    var app_force_use_portrait : Boolean = false
    // 配合 AutoSize 适配的参数，单位都是dp
    var app_landscape_screen_width : Float = 1194f
    var app_landscape_screen_height : Float = 834f
    // 如果强制使用竖屏，只需要设置下面两个竖屏的宽高即可
    var app_portrait_screen_width : Float = 375f
    var app_portrait_screen_height : Float = 812f
    // 多语言
    var language_status: String by Mmkv("LANGUAGE_STATUS", LANGUAGE_SYSTEM)
    // 项目主题
    var project_theme: Int by Mmkv("PROJECT_THEME", R.style.AppBaseTheme)
    // 深色主题
    var dark_model: Int by Mmkv("PROJECT_DARK_MODEL", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


    // 使用滑动返回
    var useSwipeBack: Boolean = true
    // 使用AutoSize进行布局适配
    var useAutoSize: Boolean = true


    // 请求url
    var BASE_URL:String = "http://101.200.207.61:9650/"


    // 请求网络的token key
    var appTokenHeadKey: String = "token"

    // 登录后保存的用户标识
    var app_token: String by Mmkv("app_token_save","")

}