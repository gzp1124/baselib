package com.thirtydays.baselibdev.net.api.impl

import com.thirtydays.baselibdev.net.api.MainApi
import com.thirtydays.baselibdev.net.service.MainService
import com.thirtydays.baselib.net.BaseImpl
import io.reactivex.Flowable
import okhttp3.ResponseBody

class MainImpl : BaseImpl<MainService>(), MainApi {

    override fun getTime(url:String): Flowable<ResponseBody> = mService.getTime(url)
}