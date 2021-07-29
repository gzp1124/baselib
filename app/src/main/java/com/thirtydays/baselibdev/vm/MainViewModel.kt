package com.thirtydays.baselibdev.vm

import androidx.lifecycle.MutableLiveData
import com.thirtydays.baselib.vm.BaseViewModel
import com.thirtydays.baselibdev.net.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel: BaseViewModel<MainRepository>() {
    override fun createRepository() = MainRepository()

    val testContent :MutableLiveData<String> by lazy {
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
    }
}