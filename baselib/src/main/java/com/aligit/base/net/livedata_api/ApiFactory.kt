package com.aligit.base.net.livedata_api

import com.aligit.base.BuildConfig
import com.aligit.base.Settings
import com.aligit.base.common.AppContext
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiFactory {
    fun makeClientBuilder(saveCookie: Boolean): OkHttpClient.Builder {
        val file = File(AppContext.cacheDir, Settings.fileSavePath.httpCachePath)
        val clientBuilder = OkHttpClient.Builder()
            .cache(Cache(file, 1024 * 1024 * 100))
            .connectTimeout(Settings.request.connectTimeout, TimeUnit.MINUTES)
            .readTimeout(Settings.request.readTimeout, TimeUnit.MINUTES)
            .writeTimeout(Settings.request.writeTimeout, TimeUnit.MINUTES)
        if (saveCookie) {
            val cookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(AppContext)
            )
            clientBuilder.cookieJar(cookieJar)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        return clientBuilder
    }

    inline fun <reified T> createString(
        baseUrl: String,
        saveCookie: Boolean,
        noinline creator: (Boolean, Int, String, String?) -> Any
    ): T {
        val clientBuilder = makeClientBuilder(saveCookie)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addCallAdapterFactory(LiveDataCallAdapterFactory<T,String>(creator))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(T::class.java)
    }

    inline fun <reified T> create(
        baseUrl: String,
        saveCookie: Boolean,
        noinline creator: (Boolean, Int, String, T?) -> Any
    ): T {
        val clientBuilder = makeClientBuilder(saveCookie)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addCallAdapterFactory(LiveDataCallAdapterFactory<T,T>(creator))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }

}