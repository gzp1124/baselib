package com.aligit.base.ext.tool

import java.text.NumberFormat

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/7/4
 * desc   :
 * </pre>
 */
/**
 * 去除小数点后面的0
 * @see removeLastZero
 */
fun String?.removeLastZero(isGroupingUsed: Boolean = false) = when (isNullOrEmpty()) {
    true -> ""
    false -> this.toDouble().removeLastZero(isGroupingUsed)
}

/**
 * 去除小数点后面的0
 * @param isGroupingUsed:是否使用千分分隔符，默认为false不使用
 */
fun Double?.removeLastZero(isGroupingUsed: Boolean = false) = when (this == null) {
    true -> ""
    false -> {
        NumberFormat.getInstance().let {
            it.isGroupingUsed = isGroupingUsed
            it.format(this) ?: ""
        }
    }
}

fun String?.getLength(): Int = when {
    this == null -> 0
    isNullOrEmpty() -> 0
    else -> length
}

fun List<Any>?.getSize(): Int = when {
    this == null -> 0
    else -> size
}