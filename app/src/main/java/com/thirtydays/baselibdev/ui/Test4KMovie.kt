package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.startWebFragment
import com.aligit.base.ext.view.click
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTest4kMovieBinding

@Route(path = "/test/4kmovie")
class Test4KMovie : BaseVmFragment<FragmentTest4kMovieBinding>(R.layout.fragment_test_4k_movie) {
    val urls = listOf<String>(
        "https://www.o8tv.com/",
        "https://ddys.tv/",
        "https://www.4kvm.com/artplayer?mvsource=0&id=5874&type=hls",
        "https://www.4kvm.com/artplayer?mvsource=0&id=5490&type=hls",
        "https://www.4kvm.com/artplayer?mvsource=0&id=20279&type=hls",
        "https://www.4kvm.com/artplayer?mvsource=0&id=62188&type=hls"
    )

    override fun onInitDataBinding() {
        mDataBinding.run {
            webView.overRegUrls.addAll(listOf(
                    "https://shp.qpic.cn/wanjiashequ_pic/0/0f3c7d3af3efda8ef4d1f1c1f26f5081/0",
                    "https://www.o8tv.com/user/reg.html",
                    "https://www.o8tv.com/user/findpass.html",
                    "https://www.o8tv.com/user/login.html"
                ))
//            webView.setWebViwClient(object : WebViewClient() {
//                // 要拦截的 url
//                val overUrls =
//
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    log("当前url:$url")
//                    if (url in overUrls) {
//                        log("拦截了")
////                        return true
//                    }
//                    return super.shouldOverrideUrlLoading(view, url)
//                }
//            })
            m1.click { loadUrl(urls[0]) }
            m2.click { loadUrl(urls[1]) }
            m3.click { loadUrl(urls[2]) }
            m4.click { loadUrl(urls[3]) }
        }
    }

    private fun loadUrl(url: String?) {
        url?.let {
            startWebFragment(it)
//            mDataBinding.run {
//                webView.clearCache(true)
//                webView.loadUrl(it)
//            }
        }
    }
}