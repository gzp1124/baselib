package com.aligit.base.net.livedata_api

import androidx.lifecycle.LiveData
import com.aligit.base.R
import com.aligit.base.ext.tool.log
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(
    private val responseType: Type,
    var creator: (Boolean, String, String, T?) -> Any
) :
    CallAdapter<T, LiveData<T>> {
    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {//确保执行一次
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val value = creator(false,"-1", t.message ?: "have error ${t.localizedMessage}", null) as T
                            postValue(value)
                        }

                        override fun onResponse(
                            call: Call<T>,
                            response: Response<T>
                        ) {
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }

    override fun responseType() = responseType
}