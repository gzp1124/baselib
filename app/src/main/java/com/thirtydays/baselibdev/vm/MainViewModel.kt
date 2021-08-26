package com.thirtydays.baselibdev.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aligit.base.ext.tool.log
import com.aligit.base.framework.mvvm.BaseViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.thirtydays.baselibdev.net.MainRepository
import com.thirtydays.baselibdev.net.testlivedataapi.TestApi

class MainViewModel : BaseViewModel() {

    private val mRepository: MainRepository = MainRepository()

    // 使用 retrofit 的写法
    /*val testContent :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    fun getTime(){
        launch {
            mRepository.getTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    testContent.value = it.string()
                }
        }
    }*/


    // 页面加载时自动执行的网络请求，和 refreshTrigger 状态进行绑定，要执行改请求就修改 refreshTrigger 的状态
    val testContent = requestData({
        TestApi.get().getVer("http://apidoc.30days-tech.com/mock/263/kelake/app/v1/account/version")
    }) {
        it
    }

    val reqData = UnPeekLiveData<String>()
    val normalData1 = requestData({
        TestApi.get().getVer("http://apidoc.30days-tech.com/mock/263/kelake/app/v1/account/version")
    },watchTag = reqData) {
        it
    }

    // 普通的网络请求，调用该方法执行，和传统的网络请求一样，通过调用的方式执行
//    fun normalGetData(){
//
//    }
}