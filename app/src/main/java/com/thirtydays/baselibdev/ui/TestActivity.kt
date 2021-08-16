package com.thirtydays.baselibdev.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityTestBinding
import com.thirtydays.baselibdev.net.bean.TestBean
import com.thirtydays.baselibdev.vm.TwoViewModel


@Route(path = "/test/test")
class TestActivity: BaseVmActivity<ActivityTestBinding>(R.layout.activity_test) {

    var testFragment: TestFragment
    var testFragment2: Test2Fragment
    init {
        testFragment = TestFragment()
        testFragment2 = Test2Fragment()
    }

    @VMScope("MainActivity")
    lateinit var twoViewModel: TwoViewModel

    @Autowired @JvmField var testBean:TestBean? = null
    @Autowired @JvmField var test: String? = null
    @Autowired @JvmField var type: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(R.id.frameLin,testFragment).commitAllowingStateLoss()

        testBean?.let {
            Log.e("gzp1124","对象过来了"+testBean?.name)
        }
    }

    override fun initData() {
        findViewById<View>(R.id.qiehuan).click {
            supportFragmentManager.beginTransaction().replace(R.id.frameLin,testFragment2).commitAllowingStateLoss()
        }
    }

    override fun onInitDataBinding() {
        mDataBinding.two = twoViewModel
    }
}