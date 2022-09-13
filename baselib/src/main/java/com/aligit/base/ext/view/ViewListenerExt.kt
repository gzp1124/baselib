@file:Suppress("UNCHECKED_CAST")

package com.aligit.base.ext.view

import android.view.View


/**
 * author : gzp1124
 * Time: 2020/9/29 23:42
 * Des:
 */

fun <T : View> T.click(block: (T) -> Unit){
    this.setOnClickListener(object :View.OnClickListener{
        override fun onClick(v: View) {
            block(v as T)
        }
    })
}

