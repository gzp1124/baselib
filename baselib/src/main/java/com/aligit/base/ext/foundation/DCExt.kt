package com.aligit.base.ext.foundation

import com.aligit.base.model.BaseResult
import com.aligit.base.net.livedata_api.IResponse

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

fun <T> IResponse<T>.dcEither() =
    resultStatus.yes {
        Either.left(resultData)
    }.otherwise {
        Either.right(BaseThrowable.InsideThrowable(errorCode.toString(), errorMessage))
    }