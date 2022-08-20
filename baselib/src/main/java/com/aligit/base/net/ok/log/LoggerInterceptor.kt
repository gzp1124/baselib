package com.aligit.base.net.ok.log

import com.blankj.utilcode.util.LogUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 * @创建者：小垚
 * @时间：2021/7/22
 * @描述：
 */
class LoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        logRequest(request)
        var response = chain.proceed(request)
        return logResponse(response, request)
    }

    private fun logRequest(request: Request) {
        try {
            var headers = request.headers

            var loggers = StringBuilder()
            loggers.appendLine("method : ${request.method}")
            loggers.appendLine("url : ${request.url}")
            if (headers != null) {
                loggers.appendLine("headers : $headers")
            }

            var requestBody = request.body
            requestBody?.let {
                var mediaType = it.contentType()
                mediaType?.let { media ->
                    if (isText(media)) {
                        loggers.appendLine("params : ${bodyToString(request)}")
                    }
                }
            }
            LogUtils.d(loggers.toString())
        } catch (e: Exception) {
            LogUtils.e(e.message)
        }
    }

    private fun logResponse(response: Response, request: Request): Response {
        try {
            var builder = response.newBuilder()
            var clone = builder.build()
            var body = clone.body
            body?.let {
                var mediaType = it.contentType()
                mediaType?.let { media ->
                    if (isText(media)) {
                        var resp = it.string()
                        var loggers = StringBuilder()
                        loggers.appendLine("{\"url\":\"${request.url}\",")
                        loggers.appendLine("\"data\":$resp}")
                        LogUtils.json(LogUtils.D, "logger", loggers.toString())
                        body = ResponseBody.create(mediaType, resp)
                        return response.newBuilder().body(body).build()
                    }
                }
            }
        } catch (e: Exception) {
            LogUtils.e(e.message)
        }
        return response
    }

    private fun isText(mediaType: MediaType?): Boolean {
        return if (mediaType == null) false else
            "text" == mediaType.subtype ||
                    "json" == mediaType.subtype ||
                    "xml" == mediaType.subtype ||
                    "html" == mediaType.subtype ||
                    "webviewhtml" == mediaType.subtype ||
                    "x-www-form-urlencoded" == mediaType.subtype
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "something error when show requestBody."
        }
    }

}