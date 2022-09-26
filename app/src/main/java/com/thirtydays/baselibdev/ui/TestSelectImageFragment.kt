package com.thirtydays.baselibdev.ui

import android.Manifest
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.bindingadapter.bindIsSelectClick
import com.aligit.base.bindingadapter.loadImage
import com.aligit.base.ext.startShowImageFragment
import com.aligit.base.ext.startWebFragment
import com.aligit.base.ext.tool.dp2px
import com.aligit.base.ext.tool.log
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.view.click
import com.aligit.base.ui.fragment.BaseVmFragment
import com.aligit.base.utils.PermissionUtil
import com.aligit.base.widget.SelectOneImageView
import com.aligit.base.widget.select_image.SelectImageView
import com.aligit.base.widget.show_grid_image.ShowGridImageView
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestSelectImageBinding
import com.xuexiang.xaop.annotation.Permission
import com.xuexiang.xaop.annotation.SingleClick


@Route(path = "/test/select_image")
class TestSelectImageFragment :
    BaseVmFragment<FragmentTestSelectImageBinding>(R.layout.fragment_test_select_image) {
    override fun onInitDataBinding() {
        mDataBinding.addLin.addView(
            SelectImageView(requireContext(), 3)
        )
        mDataBinding.addLin.addView(
            ImageView(requireContext()).apply {
                setBackgroundColor(Color.parseColor("#ff0000"))
                loadImage(
                    this,
                    "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
                )
            }, dp2px(100), dp2px(100)
        )
        mDataBinding.addLin.addView(
            ImageView(requireContext()).apply {
                setBackgroundColor(Color.parseColor("#ff0000"))
                scaleType = ImageView.ScaleType.CENTER_CROP
                loadImage(
                    this,
                    "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
                )
            }, dp2px(100), dp2px(100)
        )
        mDataBinding.addLin.addView(
            ImageView(requireContext()).apply {
                setBackgroundColor(Color.parseColor("#ff0000"))
                loadImage(
                    this,
                    "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
                    showType = 2,
                    radius = dp2px(10)
                )
            }, dp2px(100), dp2px(100)
        )
        mDataBinding.addLin.addView(
            ImageView(requireContext()).apply {
                setBackgroundColor(Color.parseColor("#ff0000"))
                scaleType = ImageView.ScaleType.CENTER_CROP
                loadImage(
                    this,
                    "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
                    showType = 2,
                    radius = dp2px(10),
                    isCenterCrop = true
                )
            }, dp2px(100), dp2px(100)
        )
        val imgs = arrayListOf(
            "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
            "https://t7.baidu.com/it/u=737555197,308540855&fm=193&f=GIF",
            "https://t7.baidu.com/it/u=2291349828,4144427007&fm=193&f=GIF"
        )
        bindIsSelectClick(mDataBinding.openimg,false)
        mDataBinding.openimg.click {
//            startWebFragment("http://www.baidu.com", showToolbar = true, toolbarTitle = "百度一下", toolbarTitleCenter = true)
            toast { "点击成功" }
//            myToast(it)
//            startShowImageFragment(imgs)
//            PermissionUtil.requestCamera({
//                toast { "请求成功" }
//            })
        }

        loadImage(
            mDataBinding.iv,
            "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
            showType = 2,
            radius = 8
        )

        mDataBinding.onlyShow.showImgs(imgs)
        mDataBinding.showLin.addView(
            ShowGridImageView(requireContext(), spanCount = 3).apply {
                showImgs(imgs)
            }
        )
    }

    @Permission(Manifest.permission.CAMERA)
    fun myToast(v: View){
//        toast { "点击啦啦啦啦啦啦啦啦啦啦" }
        log("lai l l l l l l l ")
    }
}