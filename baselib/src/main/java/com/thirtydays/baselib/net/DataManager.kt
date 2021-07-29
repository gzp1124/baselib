package com.thirtydays.baselib.net

import com.thirtydays.baselib.BaseApplication.Companion.dataManager
import com.thirtydays.baselib.ext.Mmkv
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType


class DataManager private constructor(client: OkHttpClient) {

    companion object{
        var TOKEN_KEY = "token"
        var app_token: String by Mmkv("APP_TOKEN", "")

        var BASE_URL = "http://101.200.207.61:9650/"

        @Volatile
        private var instance: DataManager? = null

        fun getInstance(client: OkHttpClient): DataManager {
            if (instance == null) {
                synchronized(DataManager::class) {
                    if (instance == null) {
                        instance = DataManager(client)
                    }
                }
            }
            return instance!!
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    /*
        具体服务实例化
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}

open class BaseImpl<Service> {
    protected var mService: Service

    init {
        mService = dataManager.create(serviceClass)
    }

    private val serviceClass: Class<Service>
        get() {
            return (javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments[0] as Class<Service>
        }
}
