package com.aligit.base.ext.foundation

import com.aligit.base.model.BaseResult

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 数据转换工具类
 * </pre>
 */
inline fun <reified T> BaseResult<T>.dcEither() =
    resultStatus.yes {
        Either.left(resultData)
    }.otherwise {
        Either.right(BaseThrowable.InsideThrowable(errorCode, errorMessage))
    }