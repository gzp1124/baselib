package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.Settings
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.click.ChangeThemeClick
import com.thirtydays.baselibdev.databinding.ActivityChangeThemeBinding

/**
 *
 * 换肤使用三方库：https://github.com/ximsfei/Android-skin-support
 * baselib 中还没有进行换肤的操作，后续项目中用到了，再去使用该三方库去实现吧
 */
@Route(path = "/change/theme")
class ChangeThemeActivity:BaseVmActivity<ActivityChangeThemeBinding>(R.layout.activity_change_theme)
    , ChangeThemeClick {
    override fun onInitDataBinding() {
        mDataBinding.click = this
    }

    override fun initData() {
    }

    /**
     * theme 0默认，1红色，2绿色，3蓝色
     */
    override fun changeTheme(theme: Int) {
        Settings.project_theme = theme
    }

}