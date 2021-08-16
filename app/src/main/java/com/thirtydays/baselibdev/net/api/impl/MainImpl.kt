package com.thirtydays.baselibdev.net.api.impl

import com.aligit.base.net.BaseImpl
import com.thirtydays.baselibdev.net.api.MainApi
import com.thirtydays.baselibdev.net.service.MainService
import io.reactivex.Flowable
import okhttp3.ResponseBody

class MainImpl : BaseImpl<MainService>(), MainApi {

    override suspend fun getTime(url:String): Flowable<ResponseBody> = mService.getTime(url)
}