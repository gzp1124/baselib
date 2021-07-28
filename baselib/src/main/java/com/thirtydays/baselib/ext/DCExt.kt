package com.thirtydays.baselib.ext

import com.thirtydays.baselib.error.CustomThrowable
import com.thirtydays.baselib.model.BaseResult
import com.thirtydays.baselib.model.Either


/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 数据转换工具类
 * </pre>
 */
inline fun <reified T> BaseResult<T>.dcEither() =
    when (resultStatus) {
        true -> Either.left(resultData)
        false -> Either.right(CustomThrowable(errorCode, errorMessage))
    }
