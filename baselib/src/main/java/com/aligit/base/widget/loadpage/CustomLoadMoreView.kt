package com.aligit.base.widget.loadpage

import android.view.View
import android.view.ViewGroup
import com.aligit.base.R
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class CustomLoadMoreView : BaseLoadMoreView() {
    override fun getLoadComplete(holder: BaseViewHolder): View =
        holder.getView(R.id.load_more_load_complete_view)

    override fun getLoadEndView(holder: BaseViewHolder): View =
        holder.getView(R.id.load_more_load_end_view)

    override fun getLoadFailView(holder: BaseViewHolder): View =
        holder.getView(R.id.load_more_load_fail_view)

    override fun getLoadingView(holder: BaseViewHolder): View =
        holder.getView(R.id.load_more_loading_view)

    override fun getRootView(parent: ViewGroup): View =
        parent.getItemView(R.layout.brvah_quick_view_load_more)
}