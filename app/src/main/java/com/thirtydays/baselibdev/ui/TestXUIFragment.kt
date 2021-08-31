package com.thirtydays.baselibdev.ui

import android.graphics.Color
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestXuiBinding

/**
 * 测试使用 XUI 库
 * 更多的功能查看：https://github.com/xuexiangjys/XUI/wiki
 *
 * 测试总结：
 *  1. XUILayout
 *
 */
@Route(path = "/test/test_xui")
class TestXUIFragment:BaseVmFragment<FragmentTestXuiBinding>(R.layout.fragment_test_xui) {
    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.xuiLayout.run {
            /*
            阴影 和 边框 同时设置，只会显示阴影，不会显示边框
            这两属性还是不能同时使用

            设置阴影的时候记得要设置背景色，不然阴影会在内部也显示
             */
            setBackgroundColor(Color.WHITE)
            // 圆角
            radius = 50
            // 阴影
            shadowElevation = 20
            shadowAlpha = 1f
            shadowColor = Color.RED
            // 边框
            setBorderColor(Color.BLUE)
            setBorderWidth(10)
        }
    }
}