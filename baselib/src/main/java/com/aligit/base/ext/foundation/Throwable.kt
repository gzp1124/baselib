package com.aligit.base.ext.foundation

import com.aligit.base.ext.tool.log


/**
 * author : gzp1124
 * Time: 2020/4/20 19:25
 * Des:
 */

sealed class BaseThrowable(
    val code: String? = null,
    message: String? = null,
    cause: Throwable? = null
) :
    Throwable(message, cause) {

    class ExternalThrowable(throwable: Throwable) :
        BaseThrowable(cause = throwable)

    class InsideThrowable(
        val errorCode: String,
        val errorMessage: String
    ) : BaseThrowable(code = errorCode, message = errorMessage)

    fun isExternal() = this is ExternalThrowable
    fun isInside() = this is InsideThrowable
}

fun BaseThrowable.onError() {
//    (AppContext.baseContext as BaseApplication).onNetError(this)
    log("========================================================")
    printStackTrace()
    log("error over ========================================================")
}