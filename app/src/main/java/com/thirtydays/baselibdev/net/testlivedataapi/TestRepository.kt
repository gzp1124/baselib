package com.thirtydays.baselibdev.net.testlivedataapi

import com.thirtydays.baselibdev.net.REQUEST_URL
import com.thirtydays.baselibdev.net.req.TestLoginReq
import kotlinx.coroutines.flow.flow

object TestRepository {

    fun getFlowVer() = flow {
        emit(TestApi.get().getFlowVer(REQUEST_URL))
    }

    fun getStrData() = flow {
        emit(TestApi.getStr().getFlowString(REQUEST_URL))
    }

    fun testGoosLogin(req: TestLoginReq) = flow {
        emit(TestApi.getGoos().goosLoginReq(req))
    }

}