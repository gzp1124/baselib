package com.aligit.base.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.LayoutRes
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aligit.base.ext.dowithTry
import com.aligit.base.framework.mvvm.BaseViewModel
import com.aligit.base.framework.mvvm.scope.injectViewModel
import com.aligit.base.ui.foundation.activity.BaseActivity
import com.aligit.base.widget.loadpage.LoadPageViewForStatus
import com.aligit.base.widget.loadpage.SimplePageViewForStatus


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

    private val statePageView = SimplePageViewForStatus()
    private var rootView: LoadPageViewForStatus? = null

    // 当前修改页面状态的 ViewModel 对象
    private var currentChangePageStateViewModel: BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        简化的写法，可以省去 layoutId
        mDataBinding = ActivityPlayPickBinding.inflate(layoutInflater)
        val rootView: View = mDataBinding!!.root
         */

        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mDataBinding.lifecycleOwner = this
//        mDataBinding.root.fitsSystemWindows = true

        var vms = injectViewModel()

        onInitDataBinding()
        mDataBinding.executePendingBindings()

        dowithTry{
            rootView = statePageView.getRootView(this).apply {
                failView.setOnClickListener { currentChangePageStateViewModel?.refresh() }
                noNetView.setOnClickListener { currentChangePageStateViewModel?.refresh() }
                emptyView.setOnClickListener { showToast("emptyView") }
                visibility = View.GONE
            }
            (mDataBinding.root as? ViewGroup)?.let {
                addRootView(rootView, it)
            }

            // 控制页面状态
            vms.forEach { vm ->
                vm.loadPageLiveData.observe(this) {
                    rootView?.let { rootView ->
                        currentChangePageStateViewModel = vm
                        // 更新状态布局，但是状态布局还没有放到页面中。
                        statePageView.convert(
                            rootView,
                            needHideViews,
                            loadPageStatus = it
                        )
                    }
                }
            }
        }

        vms = emptyList()

        initData()
    }


    private val needHideViews: MutableList<View> = arrayListOf()
    private fun addRootView(rootView: LoadPageViewForStatus?, parentView: ViewGroup) {
        if (parentView is ScrollView || parentView is NestedScrollView) {
            (parentView.getChildAt(0) as? ViewGroup)?.let { addRootView(rootView, it) }
        } else {
            needHideViews.clear()
            (parentView as? LinearLayout)?.let {
                for (i in 0 until it.childCount - 1) {
                    needHideViews.add(parentView.getChildAt(i))
                }
            }
            (parentView as? ViewGroup)?.addView(rootView, -1, -1)
        }
    }

    abstract fun onInitDataBinding()

    protected abstract fun initData()

}