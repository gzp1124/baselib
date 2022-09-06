package com.aligit.base.common.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.ActionMode
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.aligit.base.ext.tool.logi


/**
 * @author 小垚
 * @创建时间： 2020/11/23
 * @描述： 统一配置webview
 **/
class MyWebView : WebView {

    constructor(context: Context) : super(context, null, 0) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
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
            loadsImagesAutomatically = true
            defaultTextEncodingName = "UTF-8"
            setSupportZoom(false)
        }


        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null || url.startsWith("http://") || url.startsWith("https://")){
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
    }
}