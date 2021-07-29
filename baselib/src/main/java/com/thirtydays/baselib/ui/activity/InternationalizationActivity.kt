package com.thirtydays.baselib.ui.activity

import android.content.Context
import com.thirtydays.baselib.common.LanguageHelper
import com.thirtydays.baselib.Settings

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/1
 * desc   :
 * </pre>
 */
abstract class InternationalizationActivity : BaseActivity() {

    override fun attachBaseContext(newBase: Context) {
        val context = Settings.language_status.let {
            LanguageHelper.switchLanguage(newBase, it, isForce = true)
        }
        super.attachBaseContext(context)
    }
}