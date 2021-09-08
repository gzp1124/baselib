package com.aligit.base.net.retrofit

import com.aligit.base.Settings
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory private constructor(client: OkHttpClient) {

    companion object {
        @Volatile
        private var instance: RetrofitFactory? = null

        fun getInstance(client: OkHttpClient): RetrofitFactory {
            if (instance == null) {
                synchronized(RetrofitFactory::class) {
                    if (instance == null) {
                        instance = RetrofitFactory(client)
                    }
                }
            }
            return instance!!
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Settings.Request.BASE_URL)
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
