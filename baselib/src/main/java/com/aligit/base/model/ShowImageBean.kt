package com.aligit.base.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface ShowImageBean : Parcelable {
    val mUrl:String
    val mIsVideo:Boolean
}

// 使用简单方式实现序列化
@Parcelize
data class SimpleShowImageBean(
    override val mUrl:String, // 字段同名的处理方式
    val isVideo:Boolean
):ShowImageBean {

    //字段不同名的处理方式
    override val mIsVideo: Boolean
        get() = isVideo
}

/*
open class A{
}
@Parcelize
class B: A(),ShowImageBean {
    override val mUrl: String
        get() = TODO("Not yet implemented")
    override val mIsVideo: Boolean
        get() = TODO("Not yet implemented")

}
*/