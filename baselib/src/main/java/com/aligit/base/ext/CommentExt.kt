package com.aligit.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.GsonUtils

/**
 * author : gzp1124
 * Time: 2020/4/9 16:22
 * Des:
 */

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> Fragment.find(@IdRes id: Int): T = view?.findViewById(id) as T

inline fun <reified T : View> Dialog.find(@IdRes id: Int): T = findViewById(id)

fun Application.initWebViewDataDirectory() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val processName = getProcessName()
        processName?.let {
            if (packageName != it) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }
}

private fun Application.getProcessName(): String? {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningAppProcesses = manager.runningAppProcesses
    for (runningAppProcessInfo in runningAppProcesses) {
        if (runningAppProcessInfo.pid == android.os.Process.myPid()) {
            return runningAppProcessInfo.processName;
        }
    }
    return null
}

/**
 * 判断类型是否一致
 */
inline fun <reified T:Any> checkType(t:Any):Boolean{
    return T::class.java.isAssignableFrom(t.javaClass)
}

/**
 * CommonActivity 中打开 fragment
 */
fun startCommonFragment(fragmentPath:String,bundle: Bundle? = null){
    ARouter.getInstance()
        .build("/common/common")
        .withBundle("fragmentBundle",bundle)
        .withString("fragmentPath",fragmentPath)
        .navigation()
}

inline fun dowithTry(catchBlock: (e: Throwable) -> Unit = {},block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        catchBlock(e)
        e.printStackTrace()
    }
}

/**
 * 多个参数使用 let
 * 如果参数中有 null 就 执行代码块
// Will print
val (first, second, third) = guardLet("Hello", 3, Thing("Hello")) { return }
println(first)

// Will return
val (first, second, third) = guardLet("Hello", null, Thing("Hello")) { return }
println(first)
 */
inline fun <T: Any> checkNullLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}


/**
 * 多个参数使用 let
 * 如果参数中有 null 就 不 执行代码块
// Will print
ifLet("Hello", "A", 9) {
(first, second, third) ->
println(first)
}

// Won't print
ifLet("Hello", 9, null) {
(first, second, third) ->
println(first)
}
 */
inline fun <T: Any> ifNotNullLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}

inline fun <reified T> T.deepCopy() : T {
    return GsonUtils.fromJson(GsonUtils.toJson(this), T::class.java)
}