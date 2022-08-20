package com.thirtydays.baselibdev.net.testlivedataapi

import com.aligit.base.ext.tool.log
import com.thirtydays.baselibdev.net.REQUEST_URL
import com.thirtydays.baselibdev.net.req.TestLoginReq
import kotlinx.coroutines.flow.flow

object TestRepository {

    fun getFlowVer(t:Int = 0) = flow {
        log("gzp112411 列表角标："+t)
        emit(TestApi.get().getFlowVer(REQUEST_URL))
    }

    fun getStrData() = flow {
        emit(TestApi.getStr().getFlowString("https://www.free-api.com/doc/565"))
    }

    fun testGoosLogin(req: TestLoginReq) = flow {
        emit(TestApi.getGoos().goosLoginReq(req))
    }

}