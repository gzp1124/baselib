package com.thirtydays.baselibdev

import com.aligit.base.Settings
import com.aligit.base.common.BaseApplication
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.tool.postEvent
import com.aligit.base.ext.tool.toast
import com.aligit.base.model.TokenInvalidEvent
import com.blankj.utilcode.util.ActivityUtils
import com.thirtydays.baselibdev.router.startMain
import okhttp3.Request
import retrofit2.HttpException

class App: BaseApplication() {

    override fun okHttpAddHead(build: Request.Builder){
        build.addHeader("hahahaa", "ovovovovovovo  this is base")
    }

    private fun tokenErr(){
        postEvent(TokenInvalidEvent())
        ActivityUtils.finishAllActivities()
        startMain(true)
    }

    override fun onNetError(err: BaseThrowable) {
        toast { err.message ?: "" + err.cause?.localizedMessage }
        when {
            err.isExternal() -> { // 外部错误，一般是指非业务的错误，比如 空指针，json 转换异常，网络请求超时 等
                err.cause?.apply {
                    if (this is HttpException){
                        if (code() == 401){
                            tokenErr()
                            return
                        }
                    }
                }
            }
            err.isInside() -> {  // 内部错误，业务相关的异常，比如 账号无权访问，用户数据禁止修改 等等
            }
            err.isTokenErr() -> {  // token 错误，一般指 账号被顶，Token失效
                tokenErr()
            }
        }
    }

    override fun updateSettings() {
        Settings.UI.app_force_use_portrait = 1
    }
}