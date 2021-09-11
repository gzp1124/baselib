package com.aligit.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
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
import com.aligit.base.ui.foundation.fragment.BaseFragment
import com.aligit.base.widget.loadpage.LoadPageViewForStatus
import com.aligit.base.widget.loadpage.SimplePageViewForStatus

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


    private val statePageView = SimplePageViewForStatus()
    private var rootView: LoadPageViewForStatus? = null

    // 当前修改页面状态的 ViewModel 对象
    private var currentChangePageStateViewModel: BaseViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mDataBinding.lifecycleOwner = viewLifecycleOwner

        var vms = injectViewModel()

        dowithTry {
            rootView = context?.let {
                statePageView.getRootView(it).apply {
                    failView.setOnClickListener { currentChangePageStateViewModel?.refresh() }
                    noNetView.setOnClickListener { currentChangePageStateViewModel?.refresh() }
                    emptyView.setOnClickListener { showToast("emptyView") }
                    visibility = View.GONE
                }
            }
            (mDataBinding.root as? ViewGroup)?.let {
                addRootView(rootView, it)
            }

            // 控制页面状态
            vms.forEach { vm ->
                vm.loadPageLiveData.observe(viewLifecycleOwner) {
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

        return mDataBinding.root
    }

    // 修改页面状态时需要隐藏的 View，如果业务布局是 LinearLayout 那么直接把状态布局 add 进去原始布局会出异常的，所以进行原始 View 的隐藏
    private val needHideViews : MutableList<View> = arrayListOf()
    private fun addRootView(rootView: LoadPageViewForStatus?, parentView: ViewGroup) {
        if (parentView is ScrollView || parentView is NestedScrollView) {
            (parentView.getChildAt(0) as? ViewGroup)?.let { addRootView(rootView, it) }
        } else {
            needHideViews.clear()
            (parentView as? LinearLayout)?.let {
                for(i in 0 until it.childCount-1){
                   needHideViews.add(parentView.getChildAt(i))
                }
            }
            (parentView as? ViewGroup)?.addView(rootView,-1,-1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
    }

    override fun requestData() {  }

    abstract fun onInitDataBinding()

}