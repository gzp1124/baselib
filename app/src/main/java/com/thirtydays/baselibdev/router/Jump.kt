package com.thirtydays.baselibdev.router

import android.content.Context
import android.os.Bundle
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

private fun startFragment(fragmentPath:String,bundle:Bundle? = null){
    ARouter.getInstance()
        .build("/common/common")
        .withBundle("fragmentBundle",bundle)
        .withString("fragmentPath",fragmentPath)
        .navigation()
}

fun startDuLi(){
    val bundle = Bundle()
    bundle.putString("testDD","我触底反弹了")
    startFragment("/test/duli",bundle)
}

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