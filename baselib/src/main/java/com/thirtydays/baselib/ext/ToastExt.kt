package com.thirtydays.baselib.ext

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.thirtydays.baselib.AppContext
import me.drakeet.support.toast.ToastCompat

/**
 * User: milan
 * Time: 2019/9/10 15:11
 * Des:
 */
fun Context.toast(value: String) = toast { value }

inline fun toast(value: () -> String) {
    val toast = ToastCompat.makeText(AppContext, value(), Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.setText(value())
    toast.show()
}

