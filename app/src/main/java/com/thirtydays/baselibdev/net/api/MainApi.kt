package com.thirtydays.baselibdev.net.api

import io.reactivex.Flowable
import okhttp3.ResponseBody

interface MainApi {
    suspend fun getTime(url: String): Flowable<ResponseBody>
}