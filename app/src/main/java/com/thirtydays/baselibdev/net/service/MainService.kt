package com.thirtydays.baselibdev.net.service

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MainService {

    @Streaming
    @GET
    fun getTime(@Url fileUrl: String): Flowable<ResponseBody>
}