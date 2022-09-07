package com.aligit.base.widget.show_grid_image

import android.widget.ImageView
import com.aligit.base.R
import com.aligit.base.bindingadapter.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ShowGridImageAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.gv_filter_image) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setGone(R.id.iv_del,true)
            .setGone(R.id.tv_duration,true)
        val img = holder.getView<ImageView>(R.id.fiv)
        loadImage(img,item, isCenterCrop = true)
    }
}