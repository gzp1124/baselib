package com.thirtydays.baselibdev.net.api

import io.reactivex.Flowable
import okhttp3.ResponseBody

interface MainApi {
    fun getTime(url: String): Flowable<ResponseBody>
}