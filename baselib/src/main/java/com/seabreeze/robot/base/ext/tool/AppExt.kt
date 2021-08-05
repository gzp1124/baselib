@file:JvmName("AppUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatDelegate
import com.seabreeze.robot.base.Settings
import com.seabreeze.robot.base.common.BaseApplication
import java.io.File

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : App信息工具类
 * </pre>
 */
private const val UNKOWN = "unKnown"

/**
 * 获取应用版本名称，默认为本应用
 * @return 失败时返回unKnown
 */
fun Context.getAppVersionName(packageName: String = this.packageName): String {
    return try {
        if (packageName.isBlank()) {
            return UNKOWN
        } else {
            val pi = packageManager.getPackageInfo(packageName, 0)
            pi?.versionName ?: UNKOWN
        }
    } catch (e: PackageManager.NameNotFoundException) {
        UNKOWN
    }
}

/**
 * 获取应用版本号，默认为本应用
 * @return 失败时返回-1
 */
fun Context.getAppVersionCode(packageName: String = this.packageName): Int {
    return try {
        if (packageName.isBlank()) {
            -1
        } else {
            val pi = packageManager.getPackageInfo(packageName, 0)
            pi?.versionCode ?: -1
        }
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}

/**
 * 获取应用大小，单位为b，默认为本应用
 * @return 失败时返回-1
 */
fun Context.getAppSize(packageName: String = this.packageName): Long {
    return try {
        if (packageName.isBlank()) {
            -1
        } else {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            File(applicationInfo.sourceDir).length()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}

/**
 * 获取应用图标，默认为本应用
 * @return 失败时返回null
 */
fun Context.getAppIcon(packageName: String = this.packageName): Drawable? {
    return try {
        if (packageName.isBlank()) {
            null
        } else {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            applicationInfo.loadIcon(packageManager)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

/**
 * 是否深色模式
 */
fun Context.isDarkMode(): Boolean {
    val config = resources.configuration
    val uiMode = config.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return uiMode == Configuration.UI_MODE_NIGHT_YES
}

/**
 * 切换深色模式
 */
fun Context.changDarkMode(mode:Int){
    Settings.dark_model = mode
    if (Settings.dark_model == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)return
    BaseApplication.darkMode.postValue(mode)
    when(mode){
        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
