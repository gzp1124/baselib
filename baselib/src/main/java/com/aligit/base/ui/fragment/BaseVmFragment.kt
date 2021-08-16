package com.aligit.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aligit.base.ext.coroutine.observe
import com.aligit.base.ext.foundation.onError
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.framework.mvvm.scope.injectViewModel
import com.aligit.base.model.CoroutineState
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
abstract class BaseVmFragment<DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseFragment() {

    lateinit var mDataBinding: DataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mDataBinding.lifecycleOwner = viewLifecycleOwner
        injectViewModel()
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
    }

    /**
     * 监听请求状态和错误响应
     * 不请求不监听
     */
    fun initViewModelActions(mViewModel: BaseViewModel) {
        observe(mViewModel.error) {
            it.onError()
        }
        observe(mViewModel.statusLiveData) {
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

    abstract fun onInitDataBinding()
}