package com.thirtydays.baselibdev.net.testlivedataapi

import com.aligit.base.model.BaseResponse
import com.aligit.base.net.livedata_api.IResponse


/**
 * 这里只是示范不同字段的时候怎么处理，没有直接使用
 */
class TestResponse<T>(
    override val errorMessage: String,
    override val errorCode: String,
    override val resultStatus: Boolean,
    val content: T?
) : IResponse<T> {

    // 字段名称转换，框架的字段是 resultData ，项目的字段是 content ，使用该方法实现字段转换
    override val resultData: T?
        get() = content
}


/**
 * 第二种处理方式
 * 如果 BaseResponse 字段和项目字段不一致的
 */
class TestResponse2<T>(
    errMessage: String,
    errCode: Int,
    resultStatus: Boolean,
    content: T? // 字段名称转换，框架的字段是 resultData ，项目的字段是 content ，使用该方法实现字段转换
) : BaseResponse<T>(
    errMessage, errCode.toString(), resultStatus, content,
) {

}