@file:JvmName("DensityUtil")

package com.seabreeze.robot.base.ext.tool

import com.seabreeze.robot.base.common.AppContext
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 单位转换工具类
 * </pre>
 */
fun dp2px(dpValue: Float): Float {
    return AutoSizeUtils.dp2px(AppContext,dpValue).toFloat()
}

fun dp2px(dpValue: Int) = dp2px(dpValue.toFloat()).toInt()

fun sp2px(spValue: Float): Float {
    return AutoSizeUtils.sp2px(AppContext,spValue).toFloat()
}

fun sp2px(spValue: Int) = sp2px(spValue.toFloat()).toInt()
