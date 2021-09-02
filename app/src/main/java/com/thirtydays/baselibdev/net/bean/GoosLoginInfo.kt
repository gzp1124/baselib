package com.thirtydays.baselibdev.net.bean

data class GoosLoginInfo(
    var accessToken: String,
    var avatar: String,
    var email: String,
    var gender: String,
    var imId: String?,
    var imSign: String?,
    var newUser: Boolean,
    var nickname: String,
    var phoneNumber: String,
    var thirdId: String,
    var unionId: String,
    var userSig: String
)