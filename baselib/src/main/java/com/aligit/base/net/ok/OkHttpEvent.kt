package com.aligit.base.net.ok

/**
 * author : gzp1124
 * Time: 2019/12/23 14:39
 * Des:
 */
data class OkHttpEvent(
    var dnsStartTime: Long = 0,
    var dnsEndTime: Long = 0,
    var responseBodySize: Long = 0,
    var apiSuccess: Boolean = false,
    var errorReason: String? = null
)