package com.thirtydays.baselibdev.vm

import com.aligit.base.ext.tool.log
import com.aligit.base.ext.tool.toast
import com.aligit.base.framework.mvvm.BaseViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.thirtydays.baselibdev.net.req.TestLoginReq
import com.thirtydays.baselibdev.net.testlivedataapi.TestRepository

class TestViewModel: BaseViewModel() {
    /*
    对 activity 或 fragment 应该暴露 ProtectedUnPeekLiveData(只可观察) 类型 （相当于 LiveData ）
    ViewModel 内部使用 UnPeekLiveData(可编辑)  （相当于 MutableLiveData）
    防止 livedata 的状态混乱
     */
    val testData = UnPeekLiveData<String>()

    val openNewPage = UnPeekLiveData<Boolean>()



    //登录需要的参数对象
    val loginReq = TestLoginReq()
    //校验参数是否完整
    fun checkParam(str:String?,str2: String?):Boolean{
        return !str.isNullOrEmpty() && !str2.isNullOrEmpty()
    }
    //执行登录操作
    fun login(){
        requestData(TestRepository.testGoosLogin(loginReq)){
            toast { it.errorMessage.toString() }
            log("gzp112411 ---- 来这里了"+it.toString())
        }
    }
}