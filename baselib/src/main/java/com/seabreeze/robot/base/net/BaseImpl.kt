package com.seabreeze.robot.base.net

import com.seabreeze.robot.base.common.BaseApplication.Companion.retrofitFactory
import java.lang.reflect.ParameterizedType

/**
 * User: milan
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