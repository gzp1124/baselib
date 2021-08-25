package com.aligit.base.ext.foundation


inline fun <T, R> T.dowithTry(block: (T) -> R, catchBlock: () -> Unit = {}) {
    try {
        block(this)
    } catch (e: Throwable) {
        catchBlock()
        e.printStackTrace()
    }
}


