package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestShadowBinding

/**
 * 测试阴影
 *
 * 参考文章：https://juejin.cn/post/6844903507749765134
 * https://wanandroid.com/article/query?k=阴影
 */
@Route(path = "/test/shadow")
class TestShadowFragment:BaseVmFragment<FragmentTestShadowBinding>(R.layout.fragment_test_shadow) {
    override fun requestData() {
    }

    override fun onInitDataBinding() {
    }
}