package com.aligit.base.ext.foundation

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.aligit.base.Settings
import com.elvishew.xlog.XLog
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * <pre>
 * author : xinha
 * e-mail : xinhai.yao@qq.com
 * time   : 2020/07/08
 * desc   :
bucketName：  lanlinghui
Endpoint：   lanlinghui.oss-cn-hangzhou.aliyuncs.com
</pre> *
 */

data class Upload(
    var accessKeyId: String? = null,
    var accessKeySecret: String? = null,
    var bucketName: String? = null,
    var endpoint: String? = null,
    var stsToken: String? = null
)

val defaultUpload = Upload()

/**
 * 解决java调用问题
 * @param files 上传的文件
 * @param upload oss 参数，不传默认使用 Settings 中的 ossConstant
 */
@JvmName("uploadMultipleFile")
fun Context.uploadMultipleFile(files: List<File>,upload: Upload? = null): MutableList<UploadResult> {
    val _upload = upload ?: defaultUpload
    checkUploadParams(_upload)
    val latch = CountDownLatch(1)
    val runBlocking = runBlocking {
        val uploadMultipleFile = OSS.INSTANCE.uploadMultipleFile(
            this@uploadMultipleFile, _upload, files
        )
        latch.countDown()
        return@runBlocking uploadMultipleFile
    }
    latch.await()
    return runBlocking
}

fun Context.uploadSingleFile(
    file: File,
    upload: Upload? = null,
    success: (errorMsg: String) -> Unit
) {
    val _upload = upload ?: defaultUpload
    checkUploadParams(_upload)

    OSS.INSTANCE.uploadSingleFile(
        this,
        _upload,
        file,
        success,
        {
            XLog.e(it)
        },
        { _, _, _ -> })
}

fun Context.uploadSingleFile(
    file: File,
    upload: Upload? = null,
    success: (errorMsg: String) -> Unit,
    failure: (errorMsg: String) -> Unit,
) {
    val _upload = upload ?: defaultUpload
    checkUploadParams(_upload)

    OSS.INSTANCE.uploadSingleFile(
        this,
        _upload,
        file,
        success,
        failure,
        { _, _, _ -> })
}

private fun checkUploadParams(upload: Upload) {
    if (upload.bucketName.isNullOrEmpty()) {
        upload.bucketName = Settings.ossConstant.BUCKET_NAME
    }
    if (upload.endpoint.isNullOrEmpty()) {
        upload.endpoint = Settings.ossConstant.ENDPOINT
    }
    if (upload.accessKeyId.isNullOrEmpty()) {
        upload.accessKeyId = Settings.ossConstant.ACCESS_KEY_ID
    }
    if (upload.accessKeySecret.isNullOrEmpty()) {
        upload.accessKeySecret = Settings.ossConstant.ACCESS_KEY_SECRET
    }
    if (upload.stsToken.isNullOrEmpty()) {
        upload.stsToken = Settings.ossConstant.STS_TOKEN
    }
}

class OSS private constructor() {

    private lateinit var ossClient: OSSClient
    private lateinit var spareClient: com.alibaba.sdk.android.oss.OSS

    private fun initOSS(
        context: Context,
        accessKeyId: String,
        accessKeySecret: String,
        endPoint: String,
        token: String,
    ) {
        // 在移动端建议使用STS的方式初始化OSSClient。
        val credentialProvider = OSSStsTokenCredentialProvider(
            accessKeyId,
            accessKeySecret,
            token
        )
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒。
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒。
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个。
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次。
        ossClient = OSSClient(context, endPoint, credentialProvider, conf)
    }


    suspend fun uploadMultipleFile(
        context: Context,
        upload: Upload,
        files: List<File>
    ): MutableList<UploadResult> {
//            if (!this@OSS::ossClient.isInitialized) {
        initOSS(
            context,
            upload.accessKeyId!!,
            upload.accessKeySecret!!,
            upload.endpoint!!,
            upload.stsToken!!
        )
//            }
        if (files.isEmpty()) {
            return mutableListOf()
        }
        return withContext(Dispatchers.IO) {

            val deferredList = mutableListOf<Deferred<UploadResult>>()
            files.forEach { file ->
                val deferred = async {

                    val result = UploadResult(file.name)
                    if (file.exists()) {
                        // 构造上传请求。
                        val putObjectRequest =
                            PutObjectRequest(upload.bucketName, file.name, file.absolutePath)
                        val putObject = ossClient.putObject(putObjectRequest)
                        if (putObject.statusCode == 200) {
                            val url = ossClient.presignConstrainedObjectURL(
                                upload.bucketName, file.name,
                                Date(Date().time + 3600L * 1000 * 24 * 365 * 10).time
                            )
                                .split("\\?".toRegex())
                                .toTypedArray()[0]
                            result.ossUrl = url
                        } else {
                            result.error = UploadThrowable("statusCode is ${putObject.statusCode}")
                        }
                    } else {
                        result.error = UploadThrowable("file is not exists")
                    }
                    result
                }
                deferredList.add(deferred)
            }
            val resultList = mutableListOf<UploadResult>()
            deferredList.forEach {
                resultList.add(it.await())
            }
            resultList
        }
    }

    fun uploadSingleFile(
        context: Context,
        upload: Upload,
        file: File,
        success: (errorMsg: String) -> Unit,
        failure: (errorMsg: String) -> Unit = { _ -> },
        progress: (request: PutObjectRequest, currentSize: Long, totalSize: Long) -> Unit = { _, _, _ -> },
    ) {
//        if (!this@OSS::ossClient.isInitialized) {
        initOSS(
            context,
            upload.accessKeyId!!,
            upload.accessKeySecret!!,
            upload.endpoint!!,
            upload.stsToken!!
        )
//        }

        // 构造上传请求。
        val putObjectRequest = PutObjectRequest(upload.bucketName, file.name, file.absolutePath)
        // 异步上传时可以设置进度回调。
        putObjectRequest.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
            Platform.get().execute {
                progress(request, currentSize, totalSize)
            }
        }
        ossClient.asyncPutObject(
            putObjectRequest,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                    // 设置URL过期时间为10年  3600l* 1000*24*365*10
                    val expiration = Date(Date().time + 3600L * 1000 * 24 * 365 * 10)
                    // 生成URL
                    val url =
                        ossClient.presignConstrainedObjectURL(
                            upload.bucketName,
                            file.name,
                            expiration.time
                        ).split("\\?".toRegex()).toTypedArray()[0]
                    Platform.get().execute {
                        success(url)
                    }
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?,
                ) {
                    Platform.get().execute {
                        failure(clientException?.message + " " + serviceException?.rawMessage)
                    }
                }

            })
    }

    companion object {
        val INSTANCE: OSS by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OSS()
        }
    }
}

class UploadThrowable(errorMsg: String) : Throwable(errorMsg)
data class UploadResult(
    val key: String,
    var ossUrl: String? = null,
    var error: UploadThrowable? = null,
)