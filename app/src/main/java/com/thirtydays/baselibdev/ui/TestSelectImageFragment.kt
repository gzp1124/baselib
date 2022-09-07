package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.bindingadapter.loadImage
import com.aligit.base.ext.startShowImageFragment
import com.aligit.base.ext.view.click
import com.aligit.base.ui.fragment.BaseVmFragment
import com.aligit.base.widget.SelectOneImageView
import com.aligit.base.widget.select_image.SelectImageView
import com.aligit.base.widget.show_grid_image.ShowGridImageView
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestSelectImageBinding


@Route(path = "/test/select_image")
class TestSelectImageFragment:BaseVmFragment<FragmentTestSelectImageBinding>(R.layout.fragment_test_select_image) {
    override fun onInitDataBinding() {
        mDataBinding.addLin.addView(
            SelectImageView(requireContext(),3)
        )
        val imgs = arrayListOf(
            "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
            "https://t7.baidu.com/it/u=737555197,308540855&fm=193&f=GIF",
            "https://t7.baidu.com/it/u=2291349828,4144427007&fm=193&f=GIF"
        )
        mDataBinding.openimg.click {
            startShowImageFragment(imgs)
        }

        loadImage(mDataBinding.iv,"https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF", showType = 2, radius = 8)

        mDataBinding.onlyShow.showImgs(imgs)
        mDataBinding.showLin.addView(
            ShowGridImageView(requireContext(), spanCount = 3).apply {
                showImgs(imgs)
            }
        )
    }
}