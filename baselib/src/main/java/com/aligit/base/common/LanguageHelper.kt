package com.aligit.base.common

import com.aligit.base.Settings
import com.blankj.utilcode.util.LanguageUtils
import java.util.*

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
enum class LanguageStatus(val lang: String) {
    LANGUAGE_SYSTEM("system"),//系统
    LANGUAGE_GERMAN("de"),//德语
    LANGUAGE_ENGLISH("en"),//英语
    LANGUAGE_FRENCH("fr"),//法语
    LANGUAGE_ITALIAN("it"),//意大利语
    LANGUAGE_JAPANESE("ja"),//日本语
    LANGUAGE_SIMPLIFIED_CHINESE("zh")//简体中文
}

object LanguageHelper {

    /**
     * 切换语言
     */
    fun switchLanguage(language: LanguageStatus) {
        Settings.language_status = getLocal(language)
        if (Settings.language_status != LanguageUtils.getAppContextLanguage())
            LanguageUtils.applyLanguage(Settings.language_status)
    }

    fun switchLanguage(language: Locale = Settings.language_status) {
        Settings.language_status = language
        if (Settings.language_status != LanguageUtils.getAppContextLanguage())
            LanguageUtils.applyLanguage(Settings.language_status)
    }

    private fun getLocal(lang: LanguageStatus): Locale = when (lang) {
        LanguageStatus.LANGUAGE_SYSTEM -> LanguageUtils.getSystemLanguage()
        LanguageStatus.LANGUAGE_GERMAN -> Locale.GERMAN
        LanguageStatus.LANGUAGE_ENGLISH -> Locale.ENGLISH
        LanguageStatus.LANGUAGE_FRENCH -> Locale.FRENCH
        LanguageStatus.LANGUAGE_ITALIAN -> Locale.ITALIAN
        LanguageStatus.LANGUAGE_JAPANESE -> Locale.JAPANESE
        LanguageStatus.LANGUAGE_SIMPLIFIED_CHINESE -> Locale.SIMPLIFIED_CHINESE
    }
}