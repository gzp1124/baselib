package com.aligit.base.ui.fragment

import android.view.KeyEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.R
import com.aligit.base.databinding.FragmentWebBinding
import com.aligit.base.ext.tool.log
import com.aligit.base.ui.activity.CommonActivityEvent

@Route(path = "/common/web")
open class WebFragment : BaseVmFragment<FragmentWebBinding>(R.layout.fragment_web),
    CommonActivityEvent {
    protected var mUrl: String? = null
    override fun onInitDataBinding() {
        setHeadTitleView()?.let { mDataBinding.headLin.addView(it) }
        setBottomView()?.let { mDataBinding.bottomLin.addView(it) }
        mUrl = arguments?.getString("url")
        mDataBinding.run {
            mUrl?.let { webView.loadUrl(it) }
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
}