package com.aligit.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aligit.base.ui.foundation.fragment.BaseFragment

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/8/15
 * @description : Mvvm封装类
 * </pre>
 */
abstract class SimpleFragment : BaseFragment() {

    protected open lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this::mRootView.isInitialized) {
            return mRootView
        }
        mRootView = inflater.inflate(getLayoutId(), container, false)
        return mRootView
    }

    protected abstract fun getLayoutId(): Int

}