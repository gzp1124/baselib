package com.thirtydays.baselibdev

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.thirtydays.baselib.ext.click
import com.thirtydays.baselib.ui.activity.BaseMvvmActivity
import com.thirtydays.baselibdev.net.MainRepository
import com.thirtydays.baselibdev.net.bean.TestBean
import com.thirtydays.baselibdev.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_test.*


@Route(path = "/test/test")
class TestActivity: BaseMvvmActivity<MainRepository, MainViewModel>() {
    override fun createViewModel(): MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

    override fun getLayoutId(): Int  = R.layout.activity_test

    var testFragment: TestFragment
    var testFragment2: Test2Fragment
    init {
        testFragment = TestFragment()
        testFragment2 = Test2Fragment()
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

    override fun initViewModel() {
        qiehuan.click {
            supportFragmentManager.beginTransaction().replace(R.id.frameLin,testFragment2).commitAllowingStateLoss()
        }
    }

    override fun requestData() {
        mViewModel.getTime()
    }
}