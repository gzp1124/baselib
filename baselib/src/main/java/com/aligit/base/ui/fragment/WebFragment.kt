package com.aligit.base.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.R
import com.aligit.base.databinding.FragmentWebBinding
import com.aligit.base.ext.tool.getLength
import com.aligit.base.ext.tool.log
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.view.setGone
import com.aligit.base.ext.view.setVisible
import com.aligit.base.ui.activity.CommonActivityEvent
import com.aligit.base.ui.activity.CommonActivityOnResult
import java.io.File

@Route(path = "/common/web")
open class WebFragment : BaseVmFragment<FragmentWebBinding>(R.layout.fragment_web),
    CommonActivityEvent, CommonActivityOnResult {
    protected var mUrl: String = ""
    override fun onInitDataBinding() {
        mUrl = arguments?.getString("url") ?: ""
        if (mUrl.getLength() == 0){
            toast { "url is empty" }
        }else {
            mDataBinding.run {
                webView.loadUrl(mUrl)
            }
        }
        initTitle()
        setBottomView()?.let { mDataBinding.bottomLin.addView(it) }
    }

    private fun initTitle(){
        val childHead = setHeadTitleView()
        mDataBinding.run {
            childHead?.let {
                headLin.addView(it)
                headLin.setVisible()
            } ?: let {
                headLin.setGone()
            }
        }
    }

    open fun setHeadTitleView(): View? {
        return null
    }

    open fun setBottomView(): View? {
        return null
    }


    override fun requestData() {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.getAction() === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (mDataBinding.webView.canGoBack()) {
                        mDataBinding.webView.goBack()
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        // 处理 webview 中的文件选择，一般用于 webview 客服中的图片选择
        if (requestCode == 10011) {
            if (Build.VERSION.SDK_INT >= 21) {
                var results: Array<Uri>? = null
                //Check if response is positive
                if (resultCode == Activity.RESULT_OK) {
                    if (null == mDataBinding.webView.mUMA) {
                        return
                    }
                    if (intent == null) { //Capture Photo if no image available
                        if (mDataBinding.webView.mCM != null) {
                            results = arrayOf(Uri.parse(mDataBinding.webView.mCM))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
                mDataBinding.webView.mUMA!!.onReceiveValue(results)
                mDataBinding.webView.mUMA = null
            } else {
                if (null == mDataBinding.webView.mUM) return
                val result =
                    if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
                mDataBinding.webView.mUM!!.onReceiveValue(result)
                mDataBinding.webView.mUM = null
            }
        }
    }

}