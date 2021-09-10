package com.thirtydays.baselibdev.vm

import com.aligit.base.framework.mvvm.BaseViewModel
import com.thirtydays.baselibdev.net.testlivedataapi.TestRepository

class TestConvertViewModel: BaseViewModel() {

    val testContent = requestDataToLiveData(TestRepository.getFlowVer()) {
        it
    }
}