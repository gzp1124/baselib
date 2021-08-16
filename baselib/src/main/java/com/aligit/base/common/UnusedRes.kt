package com.aligit.base.common

import android.content.Context
import android.view.LayoutInflater
import com.aligit.base.R

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/8/15
 * @description : 仅仅保存 xml 不被删除
 * </pre>
 */
fun Context.unUsed() {
    LayoutInflater.from(this).inflate(R.layout.toolbar, null)
    LayoutInflater.from(this).inflate(R.layout.status_bar, null)
}
