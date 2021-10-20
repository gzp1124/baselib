@file:JvmName("LogUtil")

package com.aligit.base.ext.tool

import com.blankj.utilcode.util.LogUtils

fun _log(s: Any, t: Int = 0) {
    if (s == "") return
    LogUtils.getConfig().run {
        setBorderSwitch(true)
        stackOffset = 3
    }
    if (t == 1){
        LogUtils.i(s)
    }else {
        LogUtils.e(s)
    }
}

fun logi(s: Any) = _log(s, 1)

fun log(s: Any) = _log(s)
