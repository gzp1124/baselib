package com.aligit.base.common.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import android.webkit.WebChromeClient.FileChooserParams
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.blankj.utilcode.util.ActivityUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author 小垚
 * @创建时间： 2020/11/23
 * @描述： 统一配置webview
 **/
class MyWebView : WebView {

    constructor(context: Context) : super(context, null) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        isVerticalScrollBarEnabled = false

        settings.apply {
            builtInZoomControls = false

            setAppCacheEnabled(true)
            savePassword = false
            saveFormData = false
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            displayZoomControls = true
            allowFileAccess = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            allowContentAccess = true
            loadsImagesAutomatically = true
            defaultTextEncodingName = "UTF-8"
            setSupportZoom(false)
        }


        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null || url.startsWith("http://") || url.startsWith("https://")) {
                    return false
                }
                // webview中自定义链接拦截
//                try {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    view?.context?.startActivity(intent)
//                } catch (e: Exception) {
//                    logi("shouldOverrideUrlLoading Exception:$e")
//                }
                return true
            }
        }
        addSelFile()
    }


    var mCM: String? = null
    var mUM: ValueCallback<Uri>? = null
    var mUMA: ValueCallback<Array<Uri>>? = null
    private val FCR = 10011
    private val FILECHOOSER_RESULTCODE = 10011

    fun addSelFile(){
        //----- webview 中进行文件选择 -------
        webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (mUMA != null) {
                    mUMA!!.onReceiveValue(null)
                }
                mUMA = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(ActivityUtils.getTopActivity().packageManager) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCM)
                    } catch (ex: IOException) {
                        Log.e("Webview", "Image file creation failed", ex)
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath()
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "*/*"
                val intentArray: Array<Intent>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOf<Intent>()
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(ActivityUtils.getTopActivity(),chooserIntent, FCR,null)
                return true
            }
        }
    }

    // Create an image file
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat") val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

}