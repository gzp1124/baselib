package com.thirtydays.baselibdev.vm

import androidx.lifecycle.LiveData
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
        viewModelScope.launch {
            mRepository.getTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    testContent.value = "来自TwoVM"+it.string()
                }
        }
    }*/


    // 请求方式一：监听 livedata 自动请求
    // 页面加载时自动执行的网络请求，和 refreshTrigger 状态进行绑定，要执行改请求就修改 refreshTrigger 的状态
    val testContent = requestDataToLiveData(TestRepository.getFlowVer()) {
        it
    }

    // 请求方式二：和第一种类似，只是监听的 livedata 触发是由按钮触发
    // 使用 livedata 请求网络的第二种写法，监听其他 livedata ，点击按钮的时候 修改监听的 livedata 的值，实现请求执行
    val reqData = UnPeekLiveData<String>()
    val normalData1 = requestDataToLiveData(TestRepository.getFlowVer(),watchTag = reqData) {
        it?.downloadUrl
    }

    // 请求方式三：调用方法进行请求，最常见
    // 普通的网络请求，调用该方法执行，和传统的网络请求一样，通过调用的方式执行
    val normalData2 = MediatorLiveData<String>()
    fun requestData2(){
        requestData(TestRepository.getFlowVer()){
            normalData2.postValue(it.resultData?.downloadUrl)
            log("来这里了"+it.resultData?.downloadUrl)
        }
    }

    // 请求方式四：直接获取接口响应的字符串，不转化为 javaBean 对象，一般用于特殊接口
    // 直接返回字符串形式的，不用 IResponse
    val strData = MediatorLiveData<String>()
    fun getStrData(){
        requestData(TestRepository.getStrData()){
            strData.postValue(it)
        }
    }
}