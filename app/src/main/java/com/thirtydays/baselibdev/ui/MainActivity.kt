package com.thirtydays.baselibdev.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.ext.view.singleClick
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import com.thirtydays.baselibdev.router.startMain
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.net.bean.TestBean
import com.thirtydays.baselibdev.router.RouterPath

@Route(path = RouterPath.AppCenter.PATH_APP_MAIN)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<Button>(R.id.openPage).singleClick {
//            ARouter.getInstance().build("/test/test").withInt("type", 1)
//                .withParcelable("testBean", TestBean("回合肥市大")).navigation()
//        }
//        findViewById<Button>(R.id.changeLa).singleClick {
//            LanguageHelper.switchLanguage(this,LanguageHelper.LANGUAGE_JAPANESE,true)
//            startMain()
//        }
    }
}