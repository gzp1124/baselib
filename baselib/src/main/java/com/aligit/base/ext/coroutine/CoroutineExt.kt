package com.aligit.base.ext.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aligit.base.ext.foundation.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * author : gzp1124
 * Time: 2020/4/9 16:22
 * Des:
 */
fun ViewModel.launchUI(block: Block) =
    viewModelScope.launch {
        block()
    }

suspend fun <T : Any> coroutineRequest(block: suspend () -> Either<T, Throwable>): Either<T, Throwable> =
    try {
        block()
    } catch (e: Exception) {
        Either.right(e)
    }

internal typealias Block = suspend CoroutineScope.() -> Unit