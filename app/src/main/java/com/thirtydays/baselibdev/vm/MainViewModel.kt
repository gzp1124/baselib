package com.thirtydays.baselibdev.vm

import androidx.lifecycle.MediatorLiveData
import com.aligit.base.ext.tool.log
import com.aligit.base.framework.mvvm.BaseViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.thirtydays.baselibdev.net.MainRepository
import com.thirtydays.baselibdev.net.testlivedataapi.TestRepository

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
    val testContent = requestDataToLiveData(TestRepository.getFlowVer()) {
        it
    }

    // 使用 livedata 请求网络的第二种写法，监听其他 livedata ，点击按钮的时候 修改监听的 livedata 的值，实现请求执行
    val reqData = UnPeekLiveData<String>()
    val normalData1 = requestDataToLiveData(TestRepository.getFlowVer(),watchTag = reqData) {
        it
    }

    val normalData2 = MediatorLiveData<String>()

    fun requestData2(){
//        viewModelScope.launch {
//            flow {
//
//            }.collect {
//                normalData2.postValue(it?.resultData?.downloadUrl)
//                log("来这里了"+it?.resultData?.downloadUrl)
//            }
//        }
        requestData(TestRepository.getFlowVer()){
            normalData2.postValue(it.resultData?.downloadUrl)
            log("来这里了"+it.resultData?.downloadUrl)
        }
    }

    // 普通的网络请求，调用该方法执行，和传统的网络请求一样，通过调用的方式执行
//    fun normalGetData(){
//
//    }
}