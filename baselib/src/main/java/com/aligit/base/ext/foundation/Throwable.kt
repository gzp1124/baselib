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

    // 外部错误，一般是指非业务的错误，比如 空指针，json 转换异常，网络请求超时 等
    class ExternalThrowable(throwable: Throwable) :
        BaseThrowable(cause = throwable)

    // 内部错误，业务相关的异常，比如 账号无权访问，用户数据禁止修改 等等
    class InsideThrowable(
        val errorCode: String,
        val errorMessage: String
    ) : BaseThrowable(code = errorCode, message = errorMessage)

    // token 错误，是内部错误的一种，一般指 账号被顶，Token失效
    class TokenThrowable : BaseThrowable()

    fun isExternal() = this is ExternalThrowable
    fun isInside() = this is InsideThrowable
    fun isTokenErr() = this is TokenThrowable

    override fun toString(): String {
        return "Throwable(code=$code message=$message cause=${cause?.localizedMessage})"
    }
}

interface ParseThrowable {
    fun onNetError(throwable: BaseThrowable)
}

fun BaseThrowable.onError() {
    cause?.printStackTrace()
    log("========================================================\n${toString()}\n========================================================")
    (AppContext.baseContext as? ParseThrowable)?.onNetError(this)
}