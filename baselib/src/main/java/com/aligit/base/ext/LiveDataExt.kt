package com.aligit.base.ext

import androidx.lifecycle.MutableLiveData
import java.lang.RuntimeException


/**
 * 将字段封装到 MutableLiveData 中
 * 一般用于请求接口的参数自动校验
 * 其中字段只能是基本数据类型
 */
fun <T, LT> convertToLivaDataField(clazz: Class<T>): LT? {

    return null
}
