package com.thirtydays.baselib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.seabreeze.robot.base.vm.ModelView
import com.thirtydays.baselib.R
import com.thirtydays.baselib.ext.toast
import com.thirtydays.baselib.model.CoroutineState
import com.thirtydays.baselib.vm.BaseRepository
import com.thirtydays.baselib.vm.BaseViewModel

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : Mvvm封装类
 * </pre>
 */
abstract class BaseMvvmFragment<out Repository : BaseRepository, out ViewModel : BaseViewModel<Repository>> :
    LazyLoadFragment(), SimpleImmersionOwner, ModelView {

    val mViewModel: ViewModel by lazy {
        createViewModel()
    }

    override fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModelActions()
        return super.initRootView(inflater, container, savedInstanceState)
    }

    abstract fun createViewModel(): ViewModel

    override fun onDestroyView() {
        hideLoading()
        super.onDestroyView()
    }

    private fun initViewModelActions() {
        mViewModel.statusLiveData.observe(viewLifecycleOwner, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {
                        showLoading()
                    }
                    CoroutineState.FINISH -> {
                        hideLoading()
                    }
                    CoroutineState.ERROR -> {
                        hideLoading()
                    }
                }
            }
        })
    }

    override fun showToast(msg: String) {
        activity?.runOnUiThread { toast { msg } }
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        showProgressDialog(tip)
    }

    override fun hideLoading() {
        dismissProgressDialog()
    }

    override fun onError(throwable: Throwable) {
        hideLoading()
        throwable.message?.let { showToast(it) }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

}