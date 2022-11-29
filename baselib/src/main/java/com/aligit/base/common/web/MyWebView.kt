package com.aligit.base.common.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import com.aligit.base.common.AppContext
import com.aligit.base.ext.tool.log
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

    val overRegUrls = mutableListOf<String>(
        "https://shp.qpic.cn/wanjiashequ_pic/0/0f3c7d3af3efda8ef4d1f1c1f26f5081/0",
        "https://www.o8tv.com/user/reg.html",
        "https://www.o8tv.com/user/findpass_msg.html?ac=email",
        "https://www.o8tv.com/user/login.html"
    )

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

        // 自定义 url 拦截 --
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null) return true
                log("匹配：url=$url")
                if (url in overRegUrls){
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        setWebChromeClient()
    }

    var mCM: String? = null
    var mUM: ValueCallback<Uri>? = null
    var mUMA: ValueCallback<Array<Uri>>? = null
    private val FCR = 10011
    private val FILECHOOSER_RESULTCODE = 10011

    // 设置 WebChromeClient
    fun setWebChromeClient() {
        webChromeClient = object : WebChromeClient() {

            // ---------- 浏览器视频全屏播放  开始 ------------------------
            private var mCustomView: View? = null
            private var mCustomViewCallback: CustomViewCallback? = null
            private var mOriginalOrientation = 0
            private var mOriginalSystemUiVisibility = 0
            override fun getDefaultVideoPoster(): Bitmap? {
                return if (mCustomView == null) {
                    null
                } else BitmapFactory.decodeResource(
                    AppContext.resources,
                    2130837573
                )
            }

            override fun onHideCustomView() {
                (ActivityUtils.getTopActivity().getWindow()
                    .getDecorView() as FrameLayout).removeView(
                    this.mCustomView
                )
                this.mCustomView = null
                ActivityUtils.getTopActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(this.mOriginalSystemUiVisibility)
                ActivityUtils.getTopActivity().setRequestedOrientation(this.mOriginalOrientation)
                this.mCustomViewCallback?.onCustomViewHidden()
                this.mCustomViewCallback = null
            }

            override fun onShowCustomView(
                paramView: View,
                paramCustomViewCallback: CustomViewCallback
            ) {
                if (this.mCustomView != null) {
                    onHideCustomView()
                    return
                }
                this.mCustomView = paramView
                this.mOriginalSystemUiVisibility =
                    ActivityUtils.getTopActivity().getWindow().getDecorView()
                        .getSystemUiVisibility()
                this.mOriginalOrientation = ActivityUtils.getTopActivity().getRequestedOrientation()
                this.mCustomViewCallback = paramCustomViewCallback
                (ActivityUtils.getTopActivity().getWindow().getDecorView() as FrameLayout).addView(
                    this.mCustomView,
                    FrameLayout.LayoutParams(-1, -1)
                )
                ActivityUtils.getTopActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                // 设置为自动横屏
                ActivityUtils.getTopActivity()
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            }
            // ---------- 浏览器视频全屏播放  结束 ------------------------

            //----- webview 中进行文件选择 -------
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
                startActivityForResult(ActivityUtils.getTopActivity(), chooserIntent, FCR, null)
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