package com.aligit.base.ui.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.aligit.base.framework.mvp.BasePresenter
import com.aligit.base.framework.mvp.view.BaseView
import com.aligit.base.ui.foundation.activity.RxAppCompatActivity
import java.lang.reflect.ParameterizedType

/**
 * author : gzp1124
 * Time: 2020/3/24 16:35
 * Des: Mvp 推荐用 RxJava
 */
abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>, VB : ViewBinding> :
    RxAppCompatActivity<VB>(), BaseView<Presenter> {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter.lifecycleProvider = this

        initData()
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    protected abstract fun initData()
}
