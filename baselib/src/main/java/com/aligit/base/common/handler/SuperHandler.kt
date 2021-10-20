package com.aligit.base.common.handler

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.aligit.base.common.handler.GetDetailHandlerHelper.msgDetail
import com.elvishew.xlog.XLog
import org.json.JSONObject

class SuperHandler(looper: Looper) : Handler(looper) {
    private var mStartTime = System.currentTimeMillis()

    override fun sendMessageAtTime(msg: Message, uptimeMillis: Long): Boolean {
        val send = super.sendMessageAtTime(msg, uptimeMillis)
        if (send) {
            msgDetail[msg] =
                Log.getStackTraceString(Throwable()).replace("java.lang.Throwable", "")
        }
        return send
    }

    override fun dispatchMessage(msg: Message) {
        mStartTime = System.currentTimeMillis()
        super.dispatchMessage(msg)
        if (msgDetail.containsKey(msg)
            && Looper.myLooper() == Looper.getMainLooper()
        ) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("Msg_Cost", System.currentTimeMillis() - mStartTime)
                jsonObject.put("MsgTrace", msg.target.toString() + " " + msgDetail[msg])
                msgDetail.remove(msg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}