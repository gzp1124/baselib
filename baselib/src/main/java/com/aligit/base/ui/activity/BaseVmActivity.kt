package com.aligit.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aligit.base.framework.mvvm.scope.injectViewModel
import com.aligit.base.ui.foundation.activity.BaseActivity


/**
 * author : gzp1124
 * Time: 2020/3/24 16:35
 * Des: Mvvm 必须使用协程
 */
abstract class BaseVmActivity<DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseActivity() {

    lateinit var mDataBinding: DataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mDataBinding.lifecycleOwner = this

        injectViewModel()
        onInitDataBinding()

        initData()
    }

    abstract fun onInitDataBinding()

    protected abstract fun initData()

}