package com.aligit.base.widget.select_image

import android.content.Context
import androidx.core.content.ContextCompat
import com.aligit.base.R
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.DensityUtil

object WeChatStyle {
    fun createStyle(context: Context): PictureSelectorStyle {
        // 主体风格
        val numberSelectMainStyle = SelectMainStyle()
        numberSelectMainStyle.isSelectNumberStyle = true
        numberSelectMainStyle.isPreviewSelectNumberStyle = false
        numberSelectMainStyle.isPreviewDisplaySelectGallery = true
        numberSelectMainStyle.selectBackground = R.drawable.ps_default_num_selector
        numberSelectMainStyle.previewSelectBackground = R.drawable.ps_preview_checkbox_selector
        numberSelectMainStyle.selectNormalBackgroundResources =
            R.drawable.ps_select_complete_normal_bg
        numberSelectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)
        numberSelectMainStyle.selectNormalText = context.getString(R.string.ps_send)
        numberSelectMainStyle.adapterPreviewGalleryBackgroundResource =
            R.drawable.ps_preview_gallery_bg
        numberSelectMainStyle.adapterPreviewGalleryItemSize =
            DensityUtil.dip2px(context, 52f)
        numberSelectMainStyle.previewSelectText = context.getString(R.string.ps_select)
        numberSelectMainStyle.previewSelectTextSize = 14
        numberSelectMainStyle.previewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        numberSelectMainStyle.previewSelectMarginRight =
            DensityUtil.dip2px(context, 6f)
        numberSelectMainStyle.selectBackgroundResources = R.drawable.ps_select_complete_bg
        numberSelectMainStyle.selectText = context.getString(R.string.ps_send_num)
        numberSelectMainStyle.selectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        numberSelectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_black)
        numberSelectMainStyle.isCompleteSelectRelativeTop = true
        numberSelectMainStyle.isPreviewSelectRelativeBottom = true
        numberSelectMainStyle.isAdapterItemIncludeEdge = false

        // 头部TitleBar 风格
        val numberTitleBarStyle = TitleBarStyle()
        numberTitleBarStyle.isHideCancelButton = true
        numberTitleBarStyle.isAlbumTitleRelativeLeft = true
        numberTitleBarStyle.titleAlbumBackgroundResource = R.drawable.ps_album_bg
        numberTitleBarStyle.titleDrawableRightResource = R.drawable.ps_ic_grey_arrow
        numberTitleBarStyle.previewTitleLeftBackResource = R.drawable.ps_ic_normal_back

        // 底部NavBar 风格
        val numberBottomNavBarStyle = BottomNavBarStyle()
        numberBottomNavBarStyle.bottomPreviewNarBarBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_half_grey)
        numberBottomNavBarStyle.bottomPreviewNormalText = context.getString(R.string.ps_preview)
        numberBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_9b)
        numberBottomNavBarStyle.bottomPreviewNormalTextSize = 16
        numberBottomNavBarStyle.isCompleteCountTips = false
        numberBottomNavBarStyle.bottomPreviewSelectText = context.getString(R.string.ps_preview_num)
        numberBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)


        return PictureSelectorStyle().apply {
            titleBarStyle = numberTitleBarStyle
            bottomBarStyle = numberBottomNavBarStyle
            selectMainStyle = numberSelectMainStyle
        }
    }
}