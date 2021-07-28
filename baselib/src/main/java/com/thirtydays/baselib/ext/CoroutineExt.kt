package com.thirtydays.baselib.ext

import androidx.lifecycle.viewModelScope
import com.thirtydays.baselib.model.Either
import com.thirtydays.baselib.vm.BaseRepository
import com.thirtydays.baselib.vm.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 协程相关api
 * </pre>
 */
fun <T : BaseRepository> BaseViewModel<T>.launchUI(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch {
        block()
    }

suspend fun <T : Any> coroutineRequest(block: suspend () -> Either<T, Throwable>): Either<T, Throwable> =
    try {
        block()
    } catch (e: Exception) {
        Either.right(e)
    }