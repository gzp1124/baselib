//package com.aligit.base.framework.mvp
//
//import com.aligit.base.framework.mvp.view.IView
//import com.trello.rxlifecycle3.LifecycleProvider
//
///**
// * author : gzp1124
// * Time: 2020/4/8 10:01
// * Des:
// */
//interface IPresenter<out View : IView<IPresenter<View>>> {
//    val mView: View
//}
//
//abstract class BasePresenter<out View : IView<BasePresenter<View>>> : IPresenter<View> {
//    override lateinit var mView: @UnsafeVariance View
//
//    lateinit var lifecycleProvider: LifecycleProvider<*>
//}