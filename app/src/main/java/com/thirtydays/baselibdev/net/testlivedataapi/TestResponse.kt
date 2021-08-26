package com.thirtydays.baselibdev.net.testlivedataapi

import com.aligit.base.net.livedata_api.IResponse

class TestResponse<T>(
    override val errorMessage: String?,
    override val errorCode: Int,
    override val resultStatus: Boolean?,
    val content: T?
) : IResponse<T> {
    // 字段名称转换，框架的字段是 resultData ，项目的字段是 content ，使用该方法实现字段转换
    override val resultData: T?
        get() = content

    override fun hasMoreData(): Boolean {
        return true
    }
}