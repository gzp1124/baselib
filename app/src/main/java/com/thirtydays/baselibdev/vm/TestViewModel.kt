package com.thirtydays.baselibdev.vm

import androidx.lifecycle.MutableLiveData
import com.aligit.base.framework.mvvm.BaseViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class TestViewModel: BaseViewModel() {
    /*
    对 activity 或 fragment 应该暴露 ProtectedUnPeekLiveData(只可观察) 类型 （相当于 LiveData ）
    ViewModel 内部使用 UnPeekLiveData(可编辑)  （相当于 MutableLiveData）
    防止 livedata 的状态混乱
     */
    val testData = UnPeekLiveData<String>()

    val openNewPage = UnPeekLiveData<Boolean>()
}