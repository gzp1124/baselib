package com.aligit.base.framework.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aligit.base.ext.coroutine.Block
import com.aligit.base.ext.coroutine.launchUI
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.model.CoroutineState

class NoViewModel:BaseViewModel(){
}

abstract class BaseViewModel : ViewModel() {
    /**
     * 协程状态管理
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }
    val error = MutableLiveData<BaseThrowable>()

    fun launch(show: Boolean = true, block: Block) =
        launchUI {
            try {
                if (show) statusLiveData.postValue(CoroutineState.Loading)
                block()
                if (show) statusLiveData.postValue(CoroutineState.Finish)
            } catch (e: Exception) {
                if (show) statusLiveData.postValue(CoroutineState.Error)
                error.postValue(BaseThrowable.ExternalThrowable(e))
            }
        }

}