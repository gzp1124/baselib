package com.aligit.base.net.retrofit

import com.aligit.base.common.BaseApplication.Companion.retrofitFactory
import java.lang.reflect.ParameterizedType

/**
 * author : gzp1124
 * Time: 2019/3/27 2:12
 * Des:
 */
open class BaseImpl<Service> {
    protected var mService: Service

    init {
        mService = retrofitFactory.create(serviceClass)
    }

    private val serviceClass: Class<Service>
        get() {
            return (javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments[0] as Class<Service>
        }
}