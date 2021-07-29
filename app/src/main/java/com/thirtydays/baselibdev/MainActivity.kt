package com.thirtydays.baselibdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.thirtydays.baselib.ext.singleClick
import com.thirtydays.baselib.net.DataManager.Companion.app_token
import com.thirtydays.baselibdev.net.bean.TestBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openPage.singleClick {
            ARouter.getInstance().build("/test/test").withInt("type", 1)
                .withParcelable("testBean", TestBean("回合肥市大")).navigation()
        }
    }
}