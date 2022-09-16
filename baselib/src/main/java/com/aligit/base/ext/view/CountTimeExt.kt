package com.aligit.base.ext.view

import android.os.CountDownTimer
import android.view.View
import androidx.core.view.doOnDetach
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aligit.base.ui.foundation.activity.BaseActivity
import com.aligit.base.ui.foundation.fragment.BaseFragment

/**
 * 开启倒计时
 * 启动：mTimer.start();
 * 暂停：mTimer.pause();
 * 继续：mTimer.resume();
 * 停止：mTimer.stop();
 * 重新启动：mTimer.reset();
 * 关闭：mTimer.cancel();
 * 使用：
 * view.startDownTime(60,1){ millisUntilFinished,miao ->
 *  // millisUntilFinished 距离倒计时结束还有多少毫秒
 *  // miao 距离倒计时结束还有多少秒
 *  // 当 millisUntilFinished == 0 miao == 0 的时候，表示倒计时完全结束了，等价于 onFinish
 * }
 */
private fun startDownTime(
    totalHaoMiao: Long,
    jiangeHaoMiao: Long,
    view: View?,
    life: LifecycleOwner?,
    onTick: ((millisUntilFinished: Long, miao: Long) -> Unit)? = null
): CountDownTimer {
    val timer = object : CountDownTimer(totalHaoMiao, jiangeHaoMiao) {
        override fun onTick(millisUntilFinished: Long) {
            val miao = millisUntilFinished / 1000
            onTick?.let { it(millisUntilFinished, miao) }
        }

        override fun onFinish() {
            onTick?.let { it(0, 0) }
        }
    }
    timer.start()
    view?.doOnDetach { timer.cancel() }
    life?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            timer.cancel()
        }
    })
    return timer
}

fun BaseActivity.startDownTime(
    totalHaoMiao: Long, jiangeHaoMiao: Long,
    onTick: ((millisUntilFinished: Long, miao: Long) -> Unit)?
): CountDownTimer {
    return startDownTime(totalHaoMiao, jiangeHaoMiao, null, this, onTick)
}

fun BaseFragment.startDownTime(
    totalHaoMiao: Long, jiangeHaoMiao: Long,
    onTick: ((millisUntilFinished: Long, miao: Long) -> Unit)?
): CountDownTimer {
    return startDownTime(totalHaoMiao, jiangeHaoMiao, null, this, onTick)
}

fun View.startDownTime(
    totalHaoMiao: Long,
    jiangeHaoMiao: Long,
    onTick: ((millisUntilFinished: Long, miao: Long) -> Unit)?
): CountDownTimer {
    return startDownTime(totalHaoMiao, jiangeHaoMiao, this, null, onTick)
}