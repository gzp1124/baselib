package com.aligit.base.widget.loadpage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.aligit.base.R
import com.aligit.base.ext.find
import com.aligit.base.ext.tool.getItemView

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class SimplePageViewForStatus : BasePageViewForStatus() {
    override fun getRootView(context: Context): LoadPageViewForStatus =
        LoadPageViewForStatus(context)

    override fun getLoadingView(status: LoadPageViewForStatus) = status.loadingView

    override fun getLoadFailView(status: LoadPageViewForStatus) = status.failView

    override fun getLoadEmptyView(status: LoadPageViewForStatus) = status.emptyView

    override fun getLoadNoNetView(status: LoadPageViewForStatus) = status.noNetView

}

class LoadPageViewForStatus @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var failView: View
    var noNetView: View
    var emptyView: View
    var loadingView: View

    init {
        val itemView = getItemView(R.layout.base_state_page_view)
        failView = itemView.find(R.id.load_more_load_fail_view)
        noNetView = itemView.find(R.id.load_more_load_no_net_view)
        emptyView = itemView.find(R.id.load_more_load_empty_view)
        loadingView = itemView.find(R.id.load_more_loading_view)
        addView(itemView)
    }
}