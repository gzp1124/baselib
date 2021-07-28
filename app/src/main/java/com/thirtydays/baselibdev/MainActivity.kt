package com.thirtydays.baselibdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thirtydays.baselib.net.DataManager.Companion.app_token
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mc.text = "123"
        app_token = "123"
    }
}