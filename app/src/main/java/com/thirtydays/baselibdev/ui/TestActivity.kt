package com.thirtydays.baselibdev.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.common.AppContext
import com.seabreeze.robot.base.common.BaseApplication
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.view.click
import com.seabreeze.robot.base.framework.mvvm.scope.VMScope
import com.seabreeze.robot.base.framework.mvvm.scope.injectViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityTestBinding
import com.thirtydays.baselibdev.net.bean.TestBean
import com.thirtydays.baselibdev.vm.MainViewModel
import com.thirtydays.baselibdev.vm.TwoViewModel


@Route(path = "/test/test")
class TestActivity: BaseVmActivity<MainViewModel, ActivityTestBinding>(R.layout.activity_test) {

    var testFragment: TestFragment
    var testFragment2: Test2Fragment
    init {
        testFragment = TestFragment()
        testFragment2 = Test2Fragment()
    }

    @VMScope("test")
    lateinit var twoViewModel: TwoViewModel

    override fun createViewModel(){
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        injectViewModel()
//        twoViewModel = ViewModelProvider(this).get(TwoViewModel::class.java)
    }

    @Autowired @JvmField var testBean:TestBean? = null
    @Autowired @JvmField var test: String? = null
    @Autowired @JvmField var type: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(R.id.frameLin,if (type==1)testFragment else testFragment2).commitAllowingStateLoss()

        Log.e("gzp1124",test+"viewmodel = "+mViewModel)
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
        mDataBinding.viewModel = mViewModel
        mDataBinding.two = twoViewModel
        mViewModel.getTime()
//        twoViewModel.getTime()
    }
}