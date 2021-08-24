package com.aligit.base.net.livedata_api

// 接口响应
interface IResponse<T> {
    val data: T?
    val errMsg: String?
    val errCode: Int
}