package com.thirtydays.baselibdev.net.testlivedataapi

import com.aligit.base.net.livedata_api.IResponse

class TestResponse<T>(
    override val errMsg: String?,
    override val errCode: Int,
    val content: T?
) : IResponse<T> {
    override val data: T?
        get() = content
}