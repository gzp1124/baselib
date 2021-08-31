//package com.aligit.base.framework.mvp.view
//
//import android.graphics.Color
//import com.aligit.base.ext.foundation.BaseThrowable
//import com.aligit.base.framework.mvp.BasePresenter
//import com.aligit.base.framework.mvp.IPresenter
//
///**
// * author : gzp1124
// * Time: 2020/4/8 10:01
// * Des:
// */
//interface IView<out Presenter : IPresenter<IView<Presenter>>> {
//    val mPresenter: Presenter
////    fun showToast(msg: String)
////
////    fun showLoading(tip: String)
////
////    fun hideLoading()
////
////    fun onError(throwable: BaseThrowable)
//}
//
//interface BaseView<out Presenter : BasePresenter<BaseView<Presenter>>> : IView<Presenter>