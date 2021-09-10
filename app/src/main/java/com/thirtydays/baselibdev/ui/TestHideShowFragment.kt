package com.thirtydays.baselibdev.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestHideShowBinding

@Route(path = "/test/hide_show")
class TestHideShowFragment:BaseVmFragment<FragmentTestHideShowBinding>(R.layout.fragment_test_hide_show) {

    override fun onInitDataBinding() {
        mDataBinding.run {
            page1.click { switchpage(1) }
            page2.click { switchpage(2) }
            page3.click { switchpage(3) }
        }
    }

    var oldF:Fragment? = null
    private fun switchpage(index:Int){
        var f = childFragmentManager.findFragmentByTag("ftag$index")
        val ft = childFragmentManager.beginTransaction()
        oldF?.let { ft.hide(it) }
        if (f == null) {
            val b = Bundle()
            b.putString("title", "页面$index")
            f = TestRequestFragment()
            f.arguments = b
            ft.add(R.id.frameLin,f,"ftag$index").commitNow()
        }else{
            ft.show(f).commitNow()
        }
        oldF = f
    }

    override fun requestData() {
    }
}