package com.aligit.base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.aligit.base.framework.mvp.BasePresenter
import com.aligit.base.framework.mvp.view.BaseView
import com.aligit.base.ui.foundation.fragment.RxFragment
import java.lang.reflect.ParameterizedType

/**
 * author : gzp1124
 * Time: 2020/4/8 10:01
 * Des:
 */
abstract class BaseMvpFragment<out Presenter : BasePresenter<BaseView<Presenter>>, VB : ViewBinding> :
    RxFragment<VB>(), BaseView<Presenter> {

    final override val mPresenter: Presenter

    init {
        mPresenter = findPresenterClass().newInstance()
        mPresenter.mView = this
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<Presenter>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.lifecycleProvider = this
    }
}
