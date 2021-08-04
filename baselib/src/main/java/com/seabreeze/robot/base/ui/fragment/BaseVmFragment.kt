package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.scope.injectViewModel
import com.seabreeze.robot.base.model.CoroutineState
import com.seabreeze.robot.base.ui.foundation.fragment.BaseFragment

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
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
    fun initViewModelActions(mViewModel:BaseViewModel) {
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