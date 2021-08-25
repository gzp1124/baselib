package com.thirtydays.baselibdev.vm

import com.aligit.base.framework.mvvm.BaseViewModel
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


    val testContent = requestData({
        TestApi.get().getVer("http://apidoc.30days-tech.com/mock/263/kelake/app/v1/account/version")
    }) {
        it
    }
}