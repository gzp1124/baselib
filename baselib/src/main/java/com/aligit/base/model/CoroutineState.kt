package com.aligit.base.model

import com.aligit.base.framework.mvvm.BaseViewState

sealed class CoroutineState(var loadingTips: String? = "") : BaseViewState {

    object Loading : CoroutineState()
    object Finish : CoroutineState()
    object Error : CoroutineState()

    fun isLoading() = this is Loading
    fun isError() = this is Error
    fun isFinish() = this is Finish

}