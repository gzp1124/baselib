package com.thirtydays.baselibdev.vm

import com.aligit.base.framework.mvvm.BaseViewModel
import com.thirtydays.baselibdev.net.testlivedataapi.TestApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

class TestFlowViewModel: BaseViewModel() {

    //自动处理 backpressure ，真是香
    val refreshChannel = ConflatedBroadcastChannel<Boolean>() //channel 的缓存大小为 1

//    val autoReqFlow : StateFlow<String> = refreshChannel.asFlow()?.map {
//        TestApi.get().getFlowVer("")
//    }

}