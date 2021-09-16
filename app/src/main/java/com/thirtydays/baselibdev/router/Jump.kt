package com.thirtydays.baselibdev.router

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.router.RouterPath.AppCenter.PATH_APP_MAIN


/**
 * author : gzp1124
 * Time: 2020/4/8 13:39
 * Des:
 */
const val MAIN_TOKEN_INVALID = "main_token_invalid"

fun startMain(tokenInvalid: Boolean = false) {
    ARouter.getInstance()
        .build(PATH_APP_MAIN)
        .withBoolean(MAIN_TOKEN_INVALID, tokenInvalid)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}


@Interceptor(priority = 8)
class TestInterceptor : IInterceptor {
    override fun process(postcard: Postcard, callback: InterceptorCallback) {
//        XLog.e(postcard)
        callback.onContinue(postcard)
    }

    override fun init(context: Context?) {
    }
}