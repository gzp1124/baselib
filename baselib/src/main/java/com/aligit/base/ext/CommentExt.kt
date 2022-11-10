package com.aligit.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aligit.base.Settings
import com.aligit.base.common.AppContext
import com.aligit.base.ext.tool.log
import com.aligit.base.model.ShowImageBean
import com.aligit.base.model.SimpleShowImageBean
import com.aligit.base.ui.activity.CommonActivity
import com.blankj.utilcode.util.ActivityUtils
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

/**
 * 判断类型是否一致
 */
inline fun <reified T : Any> checkType(t: Any): Boolean {
    return T::class.java.isAssignableFrom(t.javaClass)
}

/**
 * CommonActivity 中打开 fragment
 * @param fragmentPath fragment使用的 ARoute 的 path
 * @param bundle 要传递到 fragment 中的 Bundle
 * @param useSwipeBack 使用侧滑返回，会覆盖 Settings.UI.useSwipeBack
 * @param useImmersionBar 使用沉浸式，会覆盖 Settings.UI.useImmersionBar
 * @param isHideBottom 隐藏虚拟按键，会覆盖 Settings.UI.isHideBottom
 * @param autoSizeIsBaseOnWidth autosize 基于宽度适配，会覆盖 Settings.AutoSize.autoSizeIsBaseOnWidth
 */
fun startCommonFragment(
    fragmentPath: String,
    bundle: Bundle? = null,
    useSwipeBack: Boolean = Settings.UI.useSwipeBack,
    useImmersionBar: Boolean = Settings.UI.useImmersionBar,
    isHideBottom: Boolean = !Settings.UI.hasNavigationBar,
    autoSizeIsBaseOnWidth: Boolean = Settings.AutoSize.autoSizeIsBaseOnWidth,
    showToolbar: Boolean = false,
    toolbarTitle: String = "",
    toolbarTitleCenter: Boolean = false,
) {
    val i = Intent(ActivityUtils.getTopActivity(), CommonActivity::class.java)
    i.putExtra("useSwipeBack", useSwipeBack)
    i.putExtra("useImmersionBar", useImmersionBar)
    i.putExtra("isHideBottom", isHideBottom)
    i.putExtra("autoSizeIsBaseOnWidth", autoSizeIsBaseOnWidth)
    i.putExtra("fragmentBundle", bundle)
    i.putExtra("fragmentPath", fragmentPath)

    i.putExtra("showToolbar", showToolbar)
    i.putExtra("toolbarTitle", toolbarTitle)
    i.putExtra("toolbarTitleCenter", toolbarTitleCenter)
    ActivityUtils.startActivity(i)
}

fun startWebFragment(
    url: String,
    useSystemWeb: Boolean = false,
    showToolbar: Boolean = false,
    toolbarTitle: String = "",
    toolbarTitleCenter: Boolean = false,
    fragmentPath: String = "/common/web",
    bundle: Bundle? = null,
    useSwipeBack: Boolean = Settings.UI.useSwipeBack, // 使用系统浏览器
    useImmersionBar: Boolean = Settings.UI.useImmersionBar,
    isHideBottom: Boolean = !Settings.UI.hasNavigationBar,
    autoSizeIsBaseOnWidth: Boolean = Settings.AutoSize.autoSizeIsBaseOnWidth,
) {
    if (useSystemWeb) {
        // 打开系统浏览器
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppContext.startActivity(intent)
        return
    }
    val myBundle = bundle ?: Bundle()
    myBundle.putString("url", url)
    startCommonFragment(
        fragmentPath,
        myBundle,
        useSwipeBack,
        useImmersionBar,
        isHideBottom,
        autoSizeIsBaseOnWidth,
        showToolbar,
        toolbarTitle,
        toolbarTitleCenter
    )
}

fun startShowImageFragment(
    urls: List<String>? = null, // 传递图片链接地址，方法内会转成对象列表
    imgBeans: ArrayList<ShowImageBean>? = null, // 直接传递对象列表
    currentIndex: Int = 0,
    fragmentPath: String = "/common/showimage",
    bundle: Bundle? = null,
    useSwipeBack: Boolean = Settings.UI.useSwipeBack,
    useImmersionBar: Boolean = Settings.UI.useImmersionBar,
    isHideBottom: Boolean = !Settings.UI.hasNavigationBar,
    autoSizeIsBaseOnWidth: Boolean = Settings.AutoSize.autoSizeIsBaseOnWidth,
    showToolbar: Boolean = false,
    toolbarTitle: String = "",
    toolbarTitleCenter: Boolean = false,
) {
    val myBundle = bundle ?: Bundle()
    var myImgBeans = arrayListOf<ShowImageBean>()
    if (imgBeans != null) {
        myImgBeans = imgBeans
    } else if (urls != null) {
        urls.forEach {
            myImgBeans.add(SimpleShowImageBean(it, false))
        }
    }
    myBundle.putParcelableArrayList("showimage", myImgBeans)
    myBundle.putInt("currentIndex", currentIndex)
    startCommonFragment(
        fragmentPath,
        myBundle,
        useSwipeBack,
        useImmersionBar,
        isHideBottom,
        autoSizeIsBaseOnWidth,showToolbar, toolbarTitle, toolbarTitleCenter
    )
}

inline fun dowithTry(catchBlock: (e: Throwable) -> Unit = {}, block: () -> Unit) {
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
inline fun <T : Any> checkNullLet(vararg elements: T?, closure: () -> Nothing): List<T> {
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
inline fun <T : Any> ifNotNullLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}

inline fun <reified T> T.deepCopy(): T {
    return GsonUtils.fromJson(GsonUtils.toJson(this), T::class.java)
}

fun getImageLoader() = Settings.Tools.imageLoader