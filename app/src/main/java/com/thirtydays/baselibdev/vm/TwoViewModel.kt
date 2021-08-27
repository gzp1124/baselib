package com.thirtydays.baselibdev.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aligit.base.framework.mvvm.BaseViewModel
import com.thirtydays.baselibdev.net.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class TwoViewModel: BaseViewModel() {

    private val mRepository: MainRepository = MainRepository()

    val testContent :MutableLiveData<String> by lazy {
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
    }
}