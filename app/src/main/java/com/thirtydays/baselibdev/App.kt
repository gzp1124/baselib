package com.thirtydays.baselibdev

import android.util.Log
import com.aligit.base.common.BaseApplication
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.tool.postEvent
import com.aligit.base.ext.tool.toast
import com.aligit.base.model.TokenInvalidEvent
import com.thirtydays.baselibdev.router.startMain
import retrofit2.HttpException

class App: BaseApplication() {

    override fun onNetError(err: BaseThrowable) {
        Log.e("gzp112411","发现了异常"+err.localizedMessage)
        when {
            err.isExternal() -> {
                val externalThrowable = this as BaseThrowable.ExternalThrowable
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
            err.isInside() -> {
                val insideThrowable = this as BaseThrowable.InsideThrowable
                toast { insideThrowable.errorMessage }
                if (insideThrowable.errorCode == "000000") {
                    startMain(true)
                    postEvent(TokenInvalidEvent())
                }
            }
        }
    }

    override fun updateSettings() {
    }
}