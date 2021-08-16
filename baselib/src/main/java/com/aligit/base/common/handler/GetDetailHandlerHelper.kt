package com.aligit.base.common.handler

import android.os.Message
import java.util.concurrent.ConcurrentHashMap

internal object GetDetailHandlerHelper {
    val msgDetail = ConcurrentHashMap<Message, String>()
}