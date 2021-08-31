package com.aligit.base.net.ok

import android.content.Context
import android.os.Environment
import com.aligit.base.Settings
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * author : gzp1124
 * Time: 2019/3/27 2:12
 * Des:
 */
class OkHttpManager private constructor() {


    fun initOkHttpClient(context: Context): OkHttpClient {
        //创建keyManagers
        val keyManagers = OkHttpUtils.prepareKeyManager(null, null)
        //创建TrustManager
//        val trustManagers = OkHttpUtils.prepareTrustManager()
        //创建X509TrustManager
        val manager: X509TrustManager = SafeTrustManager()

        // 创建TLS类型的SSLContext对象， that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
        // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
        sslContext.init(keyManagers, arrayOf<TrustManager>(manager), SecureRandom())

        // 设置 Log 拦截器，可以用于以后处理一些异常情况
        val logger: HttpLoggingInterceptor.Logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                com.aligit.base.ext.tool.logi(message)
            }
        }
        val interceptor = HttpLoggingInterceptor(logger)
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        //缓存
        val cacheFile = File(Environment.getDownloadCacheDirectory(), Settings.fileSavePath.httpCachePath)
        val cache = Cache(cacheFile, 1024 * 1024 * 100) //100Mb

        //url切换 ，业务APP去进行拦截
//        val urlInterceptor = object : Interceptor {
//            @Throws(IOException::class)
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val request = chain.request()
//                val newBuilder = request.newBuilder()
//                val headers = request.headers(H_NAME)
//                if (headers.isNotEmpty()) {
//                    newBuilder.removeHeader(H_NAME)
//                    val domainName = headers[0]
//                    if (!TextUtils.isEmpty(domainName)) {
//                        val httpUrl: HttpUrl? = fetchDomain(domainName)
//                        val newUrl = parseUrl(httpUrl, request.url)
//                        val newRequest = newBuilder
//                            .url(newUrl)
//                            .build()
//                        return chain.proceed(newRequest)
//                    }
//                    return chain.proceed(request)
//                }
//                return chain.proceed(request)
//            }
//        }

        val headInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val newBuilder = request.newBuilder()
                newBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
                if (Settings.app_token.isNotEmpty()) {
                    newBuilder.addHeader(Settings.appTokenHeadKey, Settings.app_token)
                        .build()
                }
                return chain.proceed(newBuilder.build())
            }
        }

        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        // 配置 client
        return OkHttpClient.Builder()
            .eventListenerFactory(OkHttpEventListener.FACTORY)
            .dns(OkHttpDNS())
            .addInterceptor(headInterceptor)// 设置拦截器
            .addInterceptor(interceptor) // 设置拦截器
            .retryOnConnectionFailure(true) // 是否重试
            .connectTimeout(5, TimeUnit.SECONDS) // 连接超时事件
            .readTimeout(20, TimeUnit.SECONDS) // 读取超时时间
            .writeTimeout(5, TimeUnit.SECONDS)
//                .addNetworkInterceptor(StethoInterceptor())
            .sslSocketFactory(sslContext!!.socketFactory, manager)
            .hostnameVerifier(SafeHostnameVerifier())
            .cookieJar(cookieJar)
            .cache(cache)
            .build()
    }

    private fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl {
        // 如果 HttpUrl.parse(url); 解析为 null 说明,url 格式不正确,正确的格式为 "https://github.com:443"
        // http 默认端口 80,https 默认端口 443 ,如果端口号是默认端口号就可以将 ":443" 去掉
        // 只支持 http 和 https
        return if (null == domainUrl) url else url.newBuilder()
            .scheme(domainUrl.scheme)
            .host(domainUrl.host)
            .port(domainUrl.port)
            .build()
    }

    companion object {
        val INSTANCE: OkHttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpManager()
        }
    }
}