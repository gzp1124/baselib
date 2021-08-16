package com.aligit.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.foundation.onError
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.framework.mvvm.scope.injectViewModel
import com.aligit.base.model.CoroutineState
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

    /**
     * 监听请求状态和错误响应
     * 不请求不监听
     */
    private fun initViewModelActions(mViewModel: BaseViewModel) {
        mViewModel.run {
            observe(error){
                it.onError()
            }
            observe(statusLiveData){
                when (it) {
                    is CoroutineState.Loading -> {
                        showProgressDialog()
                    }
                    is CoroutineState.Finish -> {
                        dismissProgressDialog()
                    }
                    is CoroutineState.Error -> {
                        dismissProgressDialog()
                    }
                }
            }
        }
    }

    protected abstract fun initData()

}