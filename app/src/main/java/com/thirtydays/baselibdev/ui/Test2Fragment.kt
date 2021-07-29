package com.thirtydays.baselibdev.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.thirtydays.baselibdev.net.MainRepository
import com.thirtydays.baselib.ui.fragment.BaseMvvmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_test.*

class Test2Fragment: BaseMvvmFragment<MainRepository, MainViewModel>() {
    override fun createViewModel() = ViewModelProvider(activity!!)[MainViewModel::class.java]

    override fun initViewModel() {
        mViewModel.testContent.observe(this, {
            mContent.text = it
        })
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("gzp1124","viewmodel = "+mViewModel)
        mContent.setTextColor(R.color.picture_color_20c064)
    }

    override fun getLayoutId(): Int  = R.layout.fragment_test

    override fun requestData() {
    }
}