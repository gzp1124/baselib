package com.thirtydays.baselibdev.net

import com.thirtydays.baselibdev.net.api.impl.MainImpl
import com.thirtydays.baselib.vm.BaseRepository
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class MainRepository : BaseRepository() {

    init {
        mainImpl = MainImpl()
    }

    companion object {
        private lateinit var mainImpl: MainImpl
    }

    suspend fun getTime(): Flowable<ResponseBody> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(1000)
            mainImpl.getTime("http://quan.suning.com/getSysTime.do")
        }
    }
}