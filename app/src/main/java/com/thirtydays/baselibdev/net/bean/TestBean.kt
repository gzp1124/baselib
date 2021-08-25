package com.thirtydays.baselibdev.net.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestBean(
    var name: String
) : Parcelable

data class VerBean(
    val checkStatus: Boolean,
    val createTime: String,
    val downloadUrl: String,
    val osType: String,
    val packageSize: String,
    val updateContent: String,
    val updateTime: String,
    val versionCode: String,
    val versionId: Int
)