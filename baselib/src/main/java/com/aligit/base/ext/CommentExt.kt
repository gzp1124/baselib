package com.aligit.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

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