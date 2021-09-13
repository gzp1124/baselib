package com.aligit.base.model

import com.aligit.base.net.livedata_api.IResponse

class StringResponse() : IResponse<String> {
    override val resultStatus: Boolean
        get() = true
    override val errorMessage: String
        get() = ""
    override val errorCode: Int
        get() = 0
    override val resultData: String?
        get() = this as? String
}

open class BaseResponse<T>(
    override val errorMessage: String,
    override val errorCode: Int,
    override val resultStatus: Boolean,
    override val resultData: T?
) : IResponse<T> {

    override fun toString(): String {
        return "BaseResponse(errorMessage=$errorMessage, errorCode=$errorCode, resultStatus=$resultStatus, resultData=$resultData)"
    }
}

/**
 * 列表数据的封装
 */
data class BasePageBean<T>(
    val data: T, // 业务数据，去掉 IResponse 后
    val page: Int,
    var hasMoreData: Boolean = true
)


data class BaseResult<T>(
    val resultStatus: Boolean,
    val errorCode: String,
    val errorMessage: String,
    val resultData: T
)