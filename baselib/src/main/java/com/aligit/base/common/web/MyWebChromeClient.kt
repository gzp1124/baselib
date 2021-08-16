package com.aligit.base.common.web

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import com.aligit.base.ext.view.setGone
import com.aligit.base.ext.view.setVisible

/**
 * author : gzp1124
 * Time: 2020/4/16 16:13
 * Des:
 */
class MyWebChromeClient(
    val context: Context,
    private val progressBar: ProgressBar,
    private val key: Int
) :
    WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        var progress = newProgress
        com.aligit.base.common.web.BroadCastManager.onProgressChanged(context, key, progress)
        progressBar.setVisible()
        if (progress == 100) {
            progress = 0
            progressBar.setGone()
        }
        progressBar.progress = progress
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        com.aligit.base.common.web.BroadCastManager.onReceivedTitle(context, key, title)
    }

    override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
        super.onReceivedTouchIconUrl(view, url, precomposed)
        com.aligit.base.common.web.BroadCastManager.onReceivedTouchIconUrl(context, key, url, precomposed)
    }

}