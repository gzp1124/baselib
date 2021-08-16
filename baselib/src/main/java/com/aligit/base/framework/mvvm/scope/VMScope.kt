package com.aligit.base.framework.mvvm.scope


/**
 * 使用注解指定 ViewModel 的作用域
 * 使用 scopeName 作为键来获取 ViewModel
 *  也就是 scopeName 相同，即可共享 ViewModel
 *  比如：AActivity 和 BActivity 的 scopeName 相同
 *  那么 A打开B页面的时候，B页面的ViewModel不会重新创建，而是会复用 A页面的
 *  这样就实现了多页面共享ViewModel
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class VMScope(val scopeName:String = "") {
}




