package com.seabreeze.robot.base

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
    // 请求url
    var BASE_URL:String = "http://101.200.207.61:9650/"

    // 多语言
    var language_status: String by Mmkv("LANGUAGE_STATUS", LANGUAGE_SYSTEM)
    // 项目主题
    var project_theme: Int by Mmkv("PROJECT_THEME", R.style.AppBaseTheme)

    // 请求网络的token key
    var appTokenHeadKey: String = "token"

    // 登录后保存的用户标识
    var app_token: String by Mmkv("app_token_save","")

    // 使用滑动返回
    var useSwipeBack: Boolean = true
}