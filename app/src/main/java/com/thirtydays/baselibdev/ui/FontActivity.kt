package com.thirtydays.baselibdev.ui

import android.os.Build
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.ActivityFontBinding

/**
 * 字体使用很简单，在 res 下创建 font 文件夹，把字体文件( otf 或者 tff )放到font文件夹中
 *      在 style 中使用 android:fontFamily="@font/NotoSansHK" 即可全局替换
 *      在代码中使用  - 暂时不使用代码了，还是直接使用 资源文件比较方便
 *
 */
@Route(path = "/test/font")
class FontActivity: BaseVmActivity<ActivityFontBinding>(R.layout.activity_font) {
    override fun onInitDataBinding() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        // getfont 最小版本26，直接从 res/font 文件夹下获取字体文件
        // mDataBinding.mContent.typeface = resources.getFont(R.font.notosanshk)

        // 从 assets 下获取字体文件
        // val tf = Typeface.createFromAsset(resources.assets, "fonts/NotoSansCJKsc-Light.otf")
        // mDataBinding.mContent.typeface = tf

        // 全局替换字体，使用 style 进行全局替换，在项目的默认样式中指定 <item name="android:fontFamily">@font/notosanshk</item>
    }
}