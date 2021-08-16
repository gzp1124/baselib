@file:JvmName("LogUtil")
package com.aligit.base.ext.tool

import com.elvishew.xlog.XLog

fun _log(s:Any, t:Int=0){
    when(t){
        1 -> XLog.i(s)
        else -> XLog.e(s)
    }
}

fun log(s:Any){
    _log(s)
}
