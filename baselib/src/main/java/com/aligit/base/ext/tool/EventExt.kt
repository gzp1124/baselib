@file:JvmName("EventUtil")

package com.aligit.base.ext.tool

import android.app.Activity
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/7/3
 * desc   :
@Subscribe(threadMode = ThreadMode.MAIN)
public void onReceiveMsg(EventMessage message) {
Log.e(TAG, "onReceiveMsg: " + message.toString());
}
</pre> *
 */
fun Activity.registerEvent() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Activity.unregisterEvent() {
    EventBus.getDefault().unregister(this)
}

fun Fragment.registerEvent() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Fragment.unregisterEvent() {
    EventBus.getDefault().unregister(this)
}

fun postEvent(event: Any) {
    EventBus.getDefault().post(event)
}