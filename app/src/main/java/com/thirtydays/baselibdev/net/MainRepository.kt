package com.thirtydays.baselibdev.net

import com.aligit.base.framework.mvvm.BaseRepository
import com.thirtydays.baselibdev.net.api.impl.MainImpl
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

/**
 * 已弃用
 *
 * 新的使用方式查看 TestResponse
 */
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
            mainImpl.getTime("http://www.baidu.com")
        }
    }
}