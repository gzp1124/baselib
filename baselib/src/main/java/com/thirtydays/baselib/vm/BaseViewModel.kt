package com.thirtydays.baselib.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirtydays.baselib.ext.Block
import com.thirtydays.baselib.model.CoroutineState
import kotlinx.coroutines.launch

abstract class BaseViewModel<out T : BaseRepository> : ViewModel() {
    protected val mRepository by lazy {
        createRepository()
    }

    abstract fun createRepository(): T

    /**
     * 协程状态管理
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }

    fun launch(show: Boolean = false, block: Block) =
        viewModelScope.launch {
            try {
                if (show) statusLiveData.value = CoroutineState.START
                block()
                if (show) statusLiveData.value = CoroutineState.FINISH
            } catch (e: Exception) {
                if (show) statusLiveData.value = CoroutineState.ERROR
                //处理协程异常
            }
        }

}