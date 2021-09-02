package com.aligit.base.model

import com.aligit.base.net.livedata_api.IResponse

class StringResponse() : IResponse<String> {
    override fun hasMoreData(): Boolean {
        return true
    }

    override val resultStatus: Boolean?
        get() = true
    override val errorMessage: String?
        get() = ""
    override val errorCode: Int?
        get() = 0
    override val resultData: String?
        get() = this as? String
}

class BaseResponse<T>(
    override val errorMessage: String?,
    override val errorCode: Int,
    override val resultStatus: Boolean?,
    override val resultData: T?
) : IResponse<T> {

    override fun hasMoreData(): Boolean {
        return true
    }

    override fun toString(): String {
        return "BaseResponse(errorMessage=$errorMessage, errorCode=$errorCode, resultStatus=$resultStatus, resultData=$resultData)"
    }
}

data class BaseResult<T>(
    val resultStatus: Boolean,
    val errorCode: String,
    val errorMessage: String,
    val resultData: T
)

data class BaseNullResult<T>(
    val resultStatus: Boolean,
    val errorCode: String?,
    val errorMessage: String?,
    val resultData: T?
)

data class ListModel<T>(
    val showData: List<T>? = null,
    val showError: String? = null,
    val showEnd: Boolean = false, // 加载更多
    val isRefresh: Boolean = false, // 刷新
    val isFinishRefresh: Boolean = true
)

data class Pager<T>(
    val current: Int,
    val pages: Int,
    val records: List<T>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int,
    var pageTime: Long
)