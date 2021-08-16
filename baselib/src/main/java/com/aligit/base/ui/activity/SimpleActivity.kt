package com.aligit.base.ui.activity

import android.os.Bundle
import com.aligit.base.ui.foundation.activity.BaseActivity

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/11/21
 * @description : 最简单
</pre> *
 */
abstract class SimpleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()
        initData()
    }

    protected open fun setLayout() {
        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()

}