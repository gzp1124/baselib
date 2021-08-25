package com.aligit.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aligit.base.R
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.base_state_page_view, container, false)
        mDataBinding.lifecycleOwner = viewLifecycleOwner
        var vms = injectViewModel()
        // 控制页面状态
        vms.forEach { vm ->
            vm.loadPageLiveData.observe(viewLifecycleOwner){
                rootView?.let { rootView ->
                    // 更新状态布局，但是状态布局还没有放到页面中。
                    statePageView.convert(
                        rootView,
                        loadPageStatus = it
                    )
                    // TODO:gzp1124 怎么样把状态布局放到页面中呢？
                }
            }
            rootView = context?.let {
                statePageView.getRootView(it).apply {
                    failView.setOnClickListener { vm.refresh() }
                    noNetView.setOnClickListener { vm.refresh() }
                    emptyView.setOnClickListener { showToast("emptyView") }
                }
            }
        }
        vms = emptyList()

        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()

    }

    abstract fun onInitDataBinding()


    private val statePageView = SimplePageViewForStatus()
    private var rootView: LoadPageViewForStatus? = null
}