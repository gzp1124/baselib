package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.view.click
import com.aligit.base.ui.fragment.BaseVmFragment
import com.aligit.base.utils.PermissionUtil
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestLocationBinding

@Route(path = "/test/getlocation")
class TestLocationFragment:BaseVmFragment<FragmentTestLocationBinding>(R.layout.fragment_test_location) {

    override fun onInitDataBinding() {
        mDataBinding.googleBtn.click {
            PermissionUtil.requestLocation({
                /*
                需要在 xml 中添加权限申请
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    在 gradle 中添加谷歌定位的包
    implementation 'com.google.android.gms:play-services-location:18.0.0'
    如下代码就能获取到经纬度

                LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            loginReq.latitude = "${it.latitude}"
                            loginReq.longitude = "${it.longitude}"

                            要使用经纬度获取更多信息的话，可以使用 AndroidUtilCode 中的工具类
                            https://github.com/Blankj/AndroidUtilCode/blob/master/lib/subutil/src/main/java/com/blankj/subutil/util/LocationUtils.java
                        }
                    }*/
            })
        }
    }
}