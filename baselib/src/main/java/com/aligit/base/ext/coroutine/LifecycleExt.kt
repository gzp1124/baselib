package com.aligit.base.ext.coroutine

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T,L:LiveData<T>> LifecycleOwner.observe(liveData: L, observer: (T) -> Unit) {
    liveData.observe(this, { it?.let { t -> observer(t) } }
    )
}

fun <T,L:MutableLiveData<T>> LifecycleOwner.observe(liveData: L, observer: (T) -> Unit) {
    liveData.observe(this, { it?.let { t -> observer(t) } }
    )
}
