package com.thirtydays.baselibdev.vm

import androidx.lifecycle.Transformations
import com.aligit.base.framework.mvvm.BaseViewModel
import com.thirtydays.baselibdev.net.testlivedataapi.TestApi
import kotlin.random.Random

class TestListViewModel: BaseViewModel() {
    //方案一    ----------------------------
    // 使用 retrofit 的方式进行请求
//    private val mRepository: MainRepository = MainRepository()
//    var dataList = MutableLiveData<MutableList<String>>()
//
//    fun getData(){
//        launch {
//            mRepository.getTime()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    if (page.value==1)dataList.value?.clear()
//                    val temp = dataList.value ?: arrayListOf()
//                    temp.addAll(arrayListOf("1","2","3","4","5"))
//                    temp.addAll(arrayListOf("1","2","3","4","5"))
//                    refreshing.postValue(false)
//                    moreLoading.postValue(false)
//                    dataList.postValue(temp)
//                }
//        }
//    }
    //方案二  使用 liveData 的方式进行请求，响应结果直接使用 livedata ，并使用 livedata 的转换方式---------------
    private val mApi = TestApi.get()

    val _dataList = Transformations.switchMap(page) {
        mApi.getLiveDataTime("http://www.baidu.com")
    }
    val dataList = Transformations.map(_dataList){
        refreshing.value = false
        moreLoading.value = false
        val list = mutableListOf<String>()
        for (i in 1..Random.nextInt(10)){
            list.add(" -- $i")
        }
//        val list = it.data ?: arrayListOf("1","2","3","4","5")
        list
    }
//    val dataList = mapPage(_dataList)
}