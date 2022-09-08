package com.aligit.base.utils

import android.Manifest
import android.content.Context
import android.os.Build
import com.aligit.base.ext.tool.getSize
import com.aligit.base.ext.tool.log
import com.aligit.base.ext.tool.toast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.UtilsTransActivity

/**
 * @创建者：gzp1124
 * @时间：2022/1/1010:21
 * @描述：权限请求的简单封装
 *
PermissionUtil.requestCamera {
pop<ScanActivity>()
}
 */
object PermissionUtil {
    val permissionDenied = { toast("permission denied") }

    fun requestPermission(
        function: () -> Unit,
        vararg permissions: String,
        denied: () -> Unit = permissionDenied
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isGranted(*permissions)) {
                PermissionUtils.permission(*permissions)
                    .rationale(object : PermissionUtils.OnRationaleListener {
                        override fun rationale(
                            activity: UtilsTransActivity,
                            shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest
                        ) {
                            shouldRequest.again(true)
                        }
                    }
                    ).callback { isAllGranted, granted, deniedForever, denied ->
                        if (!isAllGranted) {
                            denied()
                            if (deniedForever.getSize() > 0) {
                                AppUtils.launchAppDetailsSettings()
                            }
                        } else {
                            function()
                        }
                    }.request()
            } else {
                function()
            }
        } else {
            function()
        }
    }

    fun requestWrite(function: () -> Unit, denied: () -> Unit = permissionDenied) {
        requestPermission(
            function,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            denied = denied
        )
    }

    fun requestAudio(function: () -> Unit, denied: () -> Unit = permissionDenied) {
        requestPermission(function, Manifest.permission.RECORD_AUDIO, denied = denied)
    }

    fun requestCamera(function: () -> Unit, denied: () -> Unit = permissionDenied) {
        requestPermission(function, Manifest.permission.CAMERA, denied = denied)
    }

    fun requestLocation(function: () -> Unit, denied: () -> Unit = permissionDenied) {
        requestPermission(function, Manifest.permission.ACCESS_FINE_LOCATION, denied = denied)
    }
}