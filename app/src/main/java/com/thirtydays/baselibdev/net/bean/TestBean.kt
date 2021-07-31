package com.thirtydays.baselibdev.net.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestBean(
    var name: String
) : Parcelable
