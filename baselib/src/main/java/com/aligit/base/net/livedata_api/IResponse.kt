package com.aligit.base.net.livedata_api

// 接口响应
interface IResponse<T> {
    val resultStatus: Boolean? // 请求状态
    val errorMessage: String? // 错误信息
    val errorCode: Int? // 错误码
    val resultData1: T?// 响应的数据体

    //分页加载 - 有更多数据
    fun hasMoreData():Boolean
}