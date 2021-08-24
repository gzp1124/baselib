package com.thirtydays.baselibdev.net.testlivedataapi

import androidx.lifecycle.LiveData
import com.aligit.base.net.livedata_api.ApiFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface TestApi {
    companion object{
        fun get(): TestApi {
            return get(
                "http://api-service.live:8080/"
            )
        }

        fun get(url: String): TestApi {
            return ApiFactory.create(url, false) { code, msg, content ->
                TestResponse(msg, code, content)
            }
        }
    }


    @GET
    fun getLiveDataTime(@Url fileUrl:String): LiveData<TestResponse<List<String>>>
}