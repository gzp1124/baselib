package com.aligit.base.ext.foundation

import com.aligit.base.common.AppContext
import com.aligit.base.ext.tool.log


/**
 * author : gzp1124
 * Time: 2020/4/20 19:25
 * Des:
 */

sealed class BaseThrowable(
    val code: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null
) :
    Throwable(message, cause) {

    class ExternalThrowable(throwable: Throwable) :
        BaseThrowable(cause = throwable)

    class InsideThrowable(
        val errorCode: String,
        val errorMessage: String
    ) : BaseThrowable(code = errorCode, message = errorMessage)

    class TokenThrowable : BaseThrowable()

    fun isExternal() = this is ExternalThrowable
    fun isInside() = this is InsideThrowable
    fun isTokenErr() = this is TokenThrowable

    override fun toString(): String {
        return "Throwable(code=$code message=$message cause=${cause?.localizedMessage})"
    }
}

interface ParseThrowable{
    fun onNetError(throwable: BaseThrowable)
}

fun BaseThrowable.onError() {
    log("========================================================")
    log(toString())
    cause?.printStackTrace()
    log("error over ========================================================")
    (AppContext.baseContext as? ParseThrowable)?.onNetError(this)
}