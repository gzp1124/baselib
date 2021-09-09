package com.thirtydays.baselibdev.vm

import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.widget.loadpage.LoadPageStatus
import com.thirtydays.baselibdev.net.testlivedataapi.TestApi
import com.thirtydays.baselibdev.net.testlivedataapi.TestRepository
import kotlin.random.Random

class TestListViewModel : BaseViewModel() {
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
    // 分步骤写法，在baseviewmodel中进行了抽取 = requestListData
//    val _dataList = Transformations.switchMap(page) {
//        statusLiveData.postValue(CoroutineState.Loading)
//        mApi.getLiveDataTime("http://www.fsdafsdafsdafa荆防颗粒大三.com")
//    }
//    val dataList = Transformations.map(_dataList) {
//        statusLiveData.postValue(if (it.errCode == -1) CoroutineState.Error else CoroutineState.Finish)
//        refreshing.value = false
//        moreLoading.value = false
//        hasMore.value = it.hasMoreData()
//        it.data
//    }

    private val mApi = TestApi.get()
    val dataList = requestListDataToLiveData(TestRepository.getFlowVer()) { datas,page ->
        val list = mutableListOf<String>()
        for (i in 1..Random.nextInt(10)) {
            list.add(" -- $i")
        }
        list
    }

    /**
     * 使用
    loadPageLiveData.postValue(LoadPageStatus.Loading) 来控制页面状态
     */

    val xieyi = requestDataToLiveData(TestRepository.getFlowVer()){
        loadPageLiveData.postValue(LoadPageStatus.Nromal)
        it
    }

}