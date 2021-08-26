package com.thirtydays.baselibdev.click

import com.aligit.base.common.LanguageStatus

interface MainClick {
    fun openTestPage()
    fun openDuLiFragment()
    fun openSwitchLangPage()
    fun openChangeThemePage()
    fun openFontPage()
    fun changeLang(lang: LanguageStatus)
    fun openList()
}