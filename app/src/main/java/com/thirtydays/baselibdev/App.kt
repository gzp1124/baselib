package com.thirtydays.baselibdev

import com.aligit.base.Settings
import com.aligit.base.common.BaseApplication
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.tool.postEvent
import com.aligit.base.ext.tool.toast
import com.aligit.base.model.TokenInvalidEvent
import com.thirtydays.baselibdev.router.startMain
import retrofit2.HttpException

class App: BaseApplication() {

    override fun onNetError(err: BaseThrowable) {
        when {
            err.isTokenErr() -> { // token 错误，一般指 账号被顶，Token失效
                startMain(true)
                postEvent(TokenInvalidEvent())
            }
            err.isExternal() -> { // 外部错误，一般是指非业务的错误，比如 空指针，json 转换异常，网络请求超时 等
                val externalThrowable = err as BaseThrowable.ExternalThrowable
                externalThrowable.cause?.apply {
                    message?.apply {
                        toast { this }
                    }
                    if (this is HttpException) {
                        when (code()) {
                            401 -> {
                                startMain(true)
                                postEvent(TokenInvalidEvent())
                            }
                        }
                    }
                }
            }
            err.isInside() -> { // 内部错误，业务相关的异常，比如 账号无权访问，用户数据禁止修改 等等
                val insideThrowable = err as BaseThrowable.InsideThrowable
                toast { insideThrowable.errorMessage }
                if (insideThrowable.errorCode == "000000") {
                    startMain(true)
                    postEvent(TokenInvalidEvent())
                }
            }
        }
    }

    override fun updateSettings() {
        Settings.UI.app_force_use_portrait = 1
    }
}