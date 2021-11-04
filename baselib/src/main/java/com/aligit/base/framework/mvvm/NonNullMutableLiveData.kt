package com.aligit.base.framework.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class NonNullMutableLiveData<T>: MediatorLiveData<T>() {
    override fun setValue(value: T?) {
        if (value == null)return
        super.setValue(value)
    }

    fun observe(owner: LifecycleOwner, body: (T) -> Unit) {
        observe(owner, Observer<T> { t -> body(t!!) })
    }

    override fun postValue(value: T?) {
        if (value == null)return
        super.postValue(value)
    }
}