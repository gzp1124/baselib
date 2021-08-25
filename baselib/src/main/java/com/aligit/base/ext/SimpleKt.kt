package com.aligit.base.ext

import androidx.lifecycle.lifecycleScope
import com.aligit.base.ext.foundation.BaseThrowable
import com.aligit.base.ext.foundation.dcEither
import com.aligit.base.model.BaseResult
import com.aligit.base.ui.activity.SimpleActivity
import com.aligit.base.ui.fragment.SimpleFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2021/7/30
 * @description : TODO
 * </pre>
 */
inline fun <reified T> SimpleActivity.launchRequest(
    crossinline request: suspend CoroutineScope.() -> BaseResult<T>,
    crossinline success: (T) -> Unit,
    crossinline fail: (BaseThrowable) -> Unit = { onError(it) },
    flowControl: Boolean = false,
    crossinline start: () -> Unit = { showLoading() },
    crossinline end: () -> Unit = { hideLoading() },
) {
    if (flowControl) start()
    lifecycleScope.launch {
        try {
            request()
                .dcEither()
                .fold({
                    if (flowControl) end()
                    success(it)
                }, {
                    if (flowControl) end()
                    fail(it)
                })
        } catch (e: Exception) {
            if (flowControl) end()
            fail(BaseThrowable.ExternalThrowable(e))
        }
    }
}

inline fun <reified T> SimpleActivity.launchRequestIo(
    crossinline request: suspend CoroutineScope.() -> BaseResult<T>,
    crossinline success: (T) -> Unit,
    crossinline fail: (BaseThrowable) -> Unit = { onError(it) },
    flowControl: Boolean = false,
    crossinline start: () -> Unit = { showLoading() },
    crossinline end: () -> Unit = { hideLoading() },
) {
    if (flowControl) start()
    lifecycleScope.launch {
        try {
            withContext(Dispatchers.IO) {
                request()
            }
                .dcEither()
                .fold({
                    if (flowControl) end()
                    success(it)
                }, {
                    if (flowControl) end()
                    fail(it)
                })
        } catch (e: Exception) {
            if (flowControl) end()
            fail(BaseThrowable.ExternalThrowable(e))
        }
    }
}


inline fun <reified T> SimpleFragment.launchRequest(
    crossinline request: suspend CoroutineScope.() -> BaseResult<T>,
    crossinline success: (T) -> Unit,
    crossinline fail: (BaseThrowable) -> Unit = { onError(it) },
    flowControl: Boolean = false,
    crossinline start: () -> Unit = { showLoading() },
    crossinline end: () -> Unit = { hideLoading() },
) {
    if (flowControl) start()
    lifecycleScope.launch {
        try {
            request()
                .dcEither()
                .fold({
                    if (flowControl) end()
                    success(it)
                }, {
                    if (flowControl) end()
                    fail(it)
                })
        } catch (e: Exception) {
            if (flowControl) end()
            fail(BaseThrowable.ExternalThrowable(e))
        }
    }
}

inline fun <reified T> SimpleFragment.launchRequestIo(
    crossinline request: suspend CoroutineScope.() -> BaseResult<T>,
    crossinline success: (T) -> Unit,
    crossinline fail: (BaseThrowable) -> Unit = { onError(it) },
    flowControl: Boolean = false,
    crossinline start: () -> Unit = { showLoading() },
    crossinline end: () -> Unit = { hideLoading() },
) {
    if (flowControl) start()
    lifecycleScope.launch {
        try {
            withContext(Dispatchers.IO) {
                request()
            }
                .dcEither()
                .fold({
                    if (flowControl) end()
                    success(it)
                }, {
                    if (flowControl) end()
                    fail(it)
                })
        } catch (e: Exception) {
            if (flowControl) end()
            fail(BaseThrowable.ExternalThrowable(e))
        }
    }
}