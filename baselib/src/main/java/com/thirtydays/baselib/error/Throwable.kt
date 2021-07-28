package com.thirtydays.baselib.error

/**
 * User: milan
 * Time: 2020/4/20 19:25
 * Des:
 */
class CustomThrowable(
    val errorCode: String,
    val errorMessage: String
) : Throwable(errorMessage)