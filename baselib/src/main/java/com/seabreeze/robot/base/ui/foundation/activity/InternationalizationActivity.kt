package com.seabreeze.robot.base.ui.foundation.activity

import android.content.Context
import android.os.Bundle
import com.seabreeze.robot.base.Settings
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.ext.tool.changDarkMode

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/1
 * desc   : 主题、国际化
 * </pre>
 */
abstract class InternationalizationActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //主题适配
        setTheme(Settings.project_theme)
        //深色模式
        changDarkMode(Settings.dark_model)
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        //语言国际化
        val context = Settings.language_status.let {
            LanguageHelper.setLanguage(newBase, it, isForce = true)
        }
        super.attachBaseContext(context)
    }

}