package com.thirtydays.baselibdev.net.testlivedataapi

import androidx.lifecycle.LiveData
import com.aligit.base.net.livedata_api.ApiFactory
import com.thirtydays.baselibdev.net.bean.VerBean
import retrofit2.http.GET
import retrofit2.http.Url

interface TestApi {
    companion object {
        fun get(): TestApi {
            return get(
                "http://api-service.live:8080/"
            )
        }

        fun get(url: String): TestApi {
            return ApiFactory.create(url, false) { resultStatus, errorCode, errorMessage, content ->
                TestResponse(errorMessage, errorCode, resultStatus, content)
            }
        }
    }


    @GET
    fun getLiveDataTime(@Url fileUrl: String): LiveData<TestResponse<List<String>>>

    @GET
    fun getXieYi(@Url url: String): LiveData<TestResponse<String>>

    // 直接返回 livedata 的请求方式，请求和 livedata 绑定太紧了，不太合适
    @GET
    fun getVer(@Url url:String):LiveData<TestResponse<VerBean>>

    @GET
    suspend fun getFlowVer(@Url url:String):TestResponse<VerBean>
}