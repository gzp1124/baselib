package com.aligit.base.net.livedata_api

import androidx.databinding.ObservableField
import com.aligit.base.Settings
import com.aligit.base.common.AppContext
import com.aligit.base.ext.tool.logi
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object ApiFactory {

    val clientBuilder = makeClientBuilder()
    private fun makeClientBuilder(): OkHttpClient.Builder {
        val file = File(AppContext.cacheDir, Settings.fileSavePath.httpCachePath)
        val clientBuilder = OkHttpClient.Builder()
            .cache(Cache(file, 1024 * 1024 * 100))
            .connectTimeout(Settings.Request.connectTimeout, TimeUnit.MINUTES)
            .readTimeout(Settings.Request.readTimeout, TimeUnit.MINUTES)
            .writeTimeout(Settings.Request.writeTimeout, TimeUnit.MINUTES)
        //保存 cookie
        if (Settings.Request.saveCookie) {
            val cookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(AppContext)
            )
            clientBuilder.cookieJar(cookieJar)
        }
        val headInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val newBuilder = request.newBuilder()
                newBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
                //添加公用请求头
                if (Settings.Request.app_token.isNotEmpty()) {
                    newBuilder.addHeader(Settings.Request.appTokenHeadKey, Settings.Request.app_token).build()
                }
                return chain.proceed(newBuilder.build())
            }
        }
        //使用拦截器，添加公用请求头
        clientBuilder.addInterceptor(headInterceptor)

        if (Settings.isDebug) {
            // 添加 log 拦截器
            val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    logi(message)
                }
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        return clientBuilder
    }

    /**
     * 响应结果直接以原始字符串形式返回，不用 gson 处理
     */
    inline fun <reified T> createString(
        baseUrl: String,
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(T::class.java)
    }

    fun buildGsonConverterFactory(): GsonConverterFactory {
        val gsonBuilder = GsonBuilder()
        // 对请求参数进行处理，将 ObservableField 包裹的请求参数只取其中的值
        gsonBuilder.registerTypeAdapter(ObservableField::class.java, object : JsonSerializer<ObservableField<*>> {
            override fun serialize(srcDate: ObservableField<*>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
                if (srcDate == null) return null
                // get 取值
                if (srcDate.get() is Number) return JsonPrimitive(srcDate.get() as Number)
                if (srcDate.get() is Boolean) return JsonPrimitive(srcDate.get() as Boolean)
                return JsonPrimitive(srcDate.get().toString())
            }
        })
        return GsonConverterFactory.create(gsonBuilder.create())
    }

    /**
     * 响应结果对象使用 gson 处理成 JavaBean 对象
     */
    inline fun <reified T> create(
        baseUrl: String,
        noinline creator: (Boolean, String, String, T?) -> Any
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addCallAdapterFactory(LiveDataCallAdapterFactory<T>(creator))
            .addConverterFactory(buildGsonConverterFactory())
            .build()
            .create(T::class.java)
    }

}