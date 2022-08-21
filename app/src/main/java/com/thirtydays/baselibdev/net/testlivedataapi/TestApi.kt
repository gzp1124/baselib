package com.thirtydays.baselibdev.net.testlivedataapi

import androidx.lifecycle.LiveData
import com.aligit.base.model.BaseResponse
import com.aligit.base.net.livedata_api.ApiFactory
import com.thirtydays.baselibdev.net.bean.GoosLoginInfo
import com.thirtydays.baselibdev.net.bean.VerBean
import com.thirtydays.baselibdev.net.req.TestLoginReq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface TestApi {
    companion object {
        fun get(): TestApi {
            return ApiFactory.create("http://api-service.live:8080/",
                creator = { resultStatus, errorCode, errorMessage, content ->
                    BaseResponse(errorMessage, errorCode, resultStatus, content)
                },
                addHeadBlock = { build ->
                    build.addHeader("apiapiapi", "this is api")
                })
        }

        fun getGoos(): TestApi {
            return ApiFactory.create(
                "http://api.goostruck.com/goos/app/driver/v1/",
                creator = { resultStatus, errorCode, errorMessage, content ->
                    BaseResponse(errorMessage, errorCode, resultStatus, content)
                },
                addHeadBlock = { build ->
                    build.addHeader("apiapiapi", "this is api")
                })
        }

        fun getStr(): TestApi {
            return ApiFactory.createString("https://www.free-api.com/doc/565/") { build ->
                build.addHeader("apiapiapi", "this is api")
            }
        }
    }


    /**
     * 直接返回 livedata 的请求方式，请求和 livedata 绑定太紧了，不太合适 已弃用
     */
    @GET
    fun getVer(@Url url: String): LiveData<BaseResponse<VerBean>>

    /**
     * 返回结果是 IResponse 形式的
     */
    @GET
    suspend fun getFlowVer(@Url url: String): BaseResponse<VerBean>

    /**
     * 返回结果是字符串形式的
     */
    @GET
    suspend fun getFlowString(@Url url: String): String

    /**
     * 测试使用 goos 登录接口
     */
    @POST("account/login/common")
    suspend fun goosLoginReq(@Body req: TestLoginReq): BaseResponse<GoosLoginInfo>
}