package com.thirtydays.baselibdev.net.testlivedataapi

import androidx.lifecycle.LiveData
import com.aligit.base.model.BaseResponse
import com.aligit.base.net.livedata_api.ApiFactory
import com.thirtydays.baselibdev.net.bean.VerBean
import retrofit2.http.GET
import retrofit2.http.Url

interface TestApi {
    companion object {
        fun get(): TestApi {
            return ApiFactory.create("http://api-service.live:8080/", false) { resultStatus, errorCode, errorMessage, content ->
                BaseResponse(errorMessage, errorCode, resultStatus, content)
            }
        }

        fun getStr(): TestApi{
            return ApiFactory.createString("http://api-service.live:8080/",false)
        }
    }


    /**
     * 直接返回 livedata 的请求方式，请求和 livedata 绑定太紧了，不太合适 已弃用
     */
    @GET
    fun getVer(@Url url:String):LiveData<BaseResponse<VerBean>>

    /**
     * 返回结果是 IResponse 形式的
     */
    @GET
    suspend fun getFlowVer(@Url url:String):BaseResponse<VerBean>

    /**
     * 返回结果是字符串形式的
     */
    @GET
    suspend fun getFlowString(@Url url:String):String
}