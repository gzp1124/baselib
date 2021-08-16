package com.aligit.base.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.collection.ArrayMap
import kotlin.system.exitProcess

/**
 * author : gzp1124
 * Time: 2020/4/8 9:39
 * Des:
 */
object AppManager {

    private val activitySet: ArrayMap<String, Activity> = ArrayMap()

    /**
     * 当前Activity标记
     */
    private var curTag = ""

    /**
     * Activity入栈
     */
    @JvmStatic
    fun addActivity(activity: Activity) {
        com.aligit.base.common.AppManager.curTag =
            com.aligit.base.common.AppManager.getObjectTag(activity)
        com.aligit.base.common.AppManager.activitySet[com.aligit.base.common.AppManager.curTag] = activity
    }

    /**
     * Activity出栈
     */
    @JvmStatic
    fun removeActivity(activity: Activity) = com.aligit.base.common.AppManager.activitySet.remove(
        com.aligit.base.common.AppManager.getObjectTag(activity)
    )

    /**
     * Activity出栈
     */
    fun removeActivity(vararg clazz: Class<*>) {
        clazz.forEach { clz ->
            com.aligit.base.common.AppManager.activitySet.forEach {
                if (it.value.javaClass == clz) {
                    com.aligit.base.common.AppManager.removeActivity(it.value)
                }
            }
        }
    }

    /**
     * 销毁一个Activity并出栈
     */

    @JvmStatic
    fun finishActivity(activity: Activity): Activity? {
        if (!activity.isDestroyed) {
            activity.finish()
        }
        return com.aligit.base.common.AppManager.removeActivity(activity)
    }

    /**
     * 销毁Activity并出栈
     */
    @JvmStatic
    fun finishActivity(vararg clazz: Class<*>) {
        val keys = com.aligit.base.common.AppManager.activitySet.keys
        clazz.forEach { clz ->
            keys.forEach {
                val activity = com.aligit.base.common.AppManager.activitySet[it]
                if (activity != null && activity.javaClass == clz) {
                    com.aligit.base.common.AppManager.finishActivity(activity)
                }
            }
        }

    }

    /**
     * 销毁除了给定的Activity外的所有Activity
     * @param clazz:Activity白名单
     */
    @JvmStatic
    fun finishExceptActivity(vararg clazz: Class<*>) {
        val keys = com.aligit.base.common.AppManager.activitySet.keys
        clazz.forEach { clz ->
            keys.forEach {
                val activity = com.aligit.base.common.AppManager.activitySet[it]
                if (activity != null && activity.javaClass != clz) {
                    com.aligit.base.common.AppManager.finishActivity(activity)
                }
            }
        }
    }

    /**
     * 获取当前栈顶Activity
     * @return 如果栈内元素为空，则返回null
     */
    @JvmStatic
    fun currentActivity(): Activity? =
        if (com.aligit.base.common.AppManager.activitySet.isNotEmpty()) com.aligit.base.common.AppManager.activitySet[com.aligit.base.common.AppManager.curTag] else null

    /**
     * 清理栈中所有的Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        com.aligit.base.common.AppManager.activitySet.forEach {
            if (!it.value.isDestroyed) {
                it.value.finish()
            }
        }
        com.aligit.base.common.AppManager.activitySet.clear()
    }

    /**
     * 退出应用程序
     */
    @JvmStatic
    fun exitApp(context: Context) {
        com.aligit.base.common.AppManager.finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }

    /**
     * 设置栈中Activity的key
     */
    private fun getObjectTag(activity: Activity) =
        activity.javaClass.name + Integer.toHexString(activity.hashCode())
}