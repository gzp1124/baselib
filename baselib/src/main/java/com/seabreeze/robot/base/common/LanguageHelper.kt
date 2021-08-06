package com.seabreeze.robot.base.common

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringDef
import androidx.appcompat.view.ContextThemeWrapper
import com.blankj.utilcode.util.LanguageUtils
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.Settings
import java.util.*

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */

object LanguageHelper {

    @StringDef(
        LANGUAGE_SYSTEM,//系统
        LANGUAGE_GERMAN,//德语
        LANGUAGE_ENGLISH,//英语
        LANGUAGE_SPANISH,//西班牙语
        LANGUAGE_FRENCH,//法语
        LANGUAGE_ITALIAN,//意大利语
        LANGUAGE_JAPANESE,//日本语
        LANGUAGE_SIMPLIFIED_CHINESE//简体中文
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class LanguageStatus

    const val LANGUAGE_SYSTEM = "system"
    const val LANGUAGE_GERMAN = "de"
    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_SPANISH = "es"
    const val LANGUAGE_FRENCH = "fr"
    const val LANGUAGE_ITALIAN = "it"
    const val LANGUAGE_JAPANESE = "ja"
    const val LANGUAGE_SIMPLIFIED_CHINESE = "zh"

    private val LOCALE_TYPE_GERMAN = Locale.GERMAN
    private val LOCALE_TYPE_ENGLISH = Locale.ENGLISH
    private val LOCALE_TYPE_SPANISH = Locale(LANGUAGE_SPANISH)
    private val LOCALE_TYPE_FRENCH = Locale.FRENCH
    private val LOCALE_TYPE_ITALIAN = Locale.ITALIAN
    private val LOCALE_TYPE_JAPANESE = Locale.JAPANESE
    private val LOCALE_TYPE_SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE

    /**
     * 设置语言
     */
    fun setLanguage(context: Context, @LanguageStatus language: String, isForce: Boolean = false): Context {
        if (!isForce && Settings.language_status == language) {
            return context
        }
        return languageCompat(language, context)
    }

    /**
     * 切换语言
     */
    fun switchLanguage(@LanguageStatus language: String) {
        if (Settings.language_status == language) {
            return
        }
        Settings.language_status = language
        LanguageUtils.applyLanguage(getLocal())
    }

    private fun languageCompat(language: String, context: Context): Context {
        Settings.language_status = language
        return when (language) {
            LANGUAGE_SYSTEM -> languageCompat(context, systemLanguage())
            LANGUAGE_GERMAN -> languageCompat(context, LOCALE_TYPE_GERMAN)
            LANGUAGE_ENGLISH -> languageCompat(context, LOCALE_TYPE_ENGLISH)
            LANGUAGE_SPANISH -> languageCompat(context, LOCALE_TYPE_SPANISH)
            LANGUAGE_FRENCH -> languageCompat(context, LOCALE_TYPE_FRENCH)
            LANGUAGE_ITALIAN -> languageCompat(context, LOCALE_TYPE_ITALIAN)
            LANGUAGE_JAPANESE -> languageCompat(context, LOCALE_TYPE_JAPANESE)
            LANGUAGE_SIMPLIFIED_CHINESE -> languageCompat(context, LOCALE_TYPE_SIMPLIFIED_CHINESE)
            else -> context
        }
    }

    private fun languageCompat(context: Context, locale: Locale): Context {
        var ctx = context
        Locale.setDefault(locale)

        //在非application切换语言同时切换掉applicant
        if (ctx !is Application) {
            val appContext = ctx.applicationContext
            updateConfiguration(appContext, locale, Settings.language_status)
        }
        ctx = updateConfiguration(context, locale, Settings.language_status)
        val configuration = context.resources.configuration
        //兼容appcompat 1.2.0后切换语言失效问题
        return object : ContextThemeWrapper(ctx, R.style.AppBaseTheme) {
            override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
                overrideConfiguration?.setTo(configuration)
                super.applyOverrideConfiguration(overrideConfiguration)
            }
        }
    }

    private fun updateConfiguration(context: Context, locale: Locale, language: String): Context {
        var ctx = context
        val resources = ctx.resources
        val configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            ctx = ctx.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
        }
        resources.updateConfiguration(configuration, displayMetrics)
        return ctx
    }

    private fun systemLanguage(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 解决了获取系统默认错误的问题
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale.getDefault()
        }
    }

    fun getLocal(): Locale = when (Settings.language_status) {
        LANGUAGE_SYSTEM -> LOCALE_TYPE_SIMPLIFIED_CHINESE
        LANGUAGE_GERMAN -> LOCALE_TYPE_GERMAN
        LANGUAGE_ENGLISH -> LOCALE_TYPE_ENGLISH
        LANGUAGE_SPANISH -> LOCALE_TYPE_SPANISH
        LANGUAGE_FRENCH -> LOCALE_TYPE_FRENCH
        LANGUAGE_ITALIAN -> LOCALE_TYPE_ITALIAN
        LANGUAGE_JAPANESE -> LOCALE_TYPE_JAPANESE
        LANGUAGE_SIMPLIFIED_CHINESE -> LOCALE_TYPE_SIMPLIFIED_CHINESE
        else -> LOCALE_TYPE_SIMPLIFIED_CHINESE
    }
}