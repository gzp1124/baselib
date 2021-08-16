package com.aligit.base.model

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/7/3
 * desc   :
 * </pre>
 */
open class BaseEvent<T>(var bean: T? = null)

class TokenInvalidEvent() : BaseEvent<Any>()