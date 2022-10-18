package com.aligit.base.widget

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.aligit.base.R
import com.aligit.base.bindingadapter.loadImage
import com.aligit.base.ext.getImageLoader
import com.aligit.base.ext.tool.getLength
import com.aligit.base.ext.tool.getMyRealPath
import com.aligit.base.ext.tool.getSize
import com.aligit.base.ext.tool.logi
import com.aligit.base.ext.view.click
import com.aligit.base.ext.view.setGone
import com.aligit.base.ext.view.setVisible
import com.aligit.base.widget.select_image.ExoPlayerEngine
import com.aligit.base.widget.select_image.WeChatStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.thread.PictureThreadUtils
import com.luck.picture.lib.utils.MediaUtils
import com.luck.picture.lib.utils.PictureFileUtils

/**
 * 选择一张图片的控件
 * 一般用于选择图片
 * xml 和 代码中都可以使用
<com.aligit.base.widget.SelectOneImageView
app:openType="1"
android:layout_width="200dp"
android:layout_height="100dp"/>
代码中：
lin.addView(SelectOneImageView(requireContext(), openType = 1))
 */
class SelectOneImageView : RelativeLayout {

    private var showMedia: LocalMedia? = null
    private var openType = SelectMimeType.ofAll()
    private var delImage: Int = 0
    private var addImage: Int = 0
    private var canPreView: Boolean = true // 能发预览图片
    private var canPreViewDownload: Boolean = false // 预览时能否长按下载
    private var canPreViewDelete: Boolean = true // 预览时能否删除图片
    private var canDelete: Boolean = true // 选择图片后能否删除
    private var isCenterCrop: Boolean = false
    private var autoShowImg:Boolean = true // 选择后自动显示图片，设置为 false 后要手动调用显示图片

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    // 代码初始化 View
    constructor(
        context: Context,
        openType: Int = SelectMimeType.ofAll(),
        delImage: Int = 0,
        addImage: Int = 0,
        canPreView: Boolean = true,
        canDelete: Boolean = true,
        canPreViewDownload: Boolean = false,
        canPreViewDelete: Boolean = true,
        isCenterCrop: Boolean = false,
        autoShowImg: Boolean = false
    ) : super(context) {
        this.openType = openType
        this.delImage = delImage
        this.addImage = addImage
        this.canPreView = canPreView
        this.canDelete = canDelete
        this.canPreViewDownload = canPreViewDownload
        this.canPreViewDelete = canPreViewDelete
        this.isCenterCrop = isCenterCrop
        this.autoShowImg = autoShowImg
        init(context, null)
    }

    // xml 初始化 View
    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SelectOneImageView)
        openType = ta.getInteger(R.styleable.SelectOneImageView_openType, 0)
        delImage =
            ta.getResourceId(R.styleable.SelectOneImageView_delImage, R.drawable.ic_delete_menu)
        addImage =
            ta.getResourceId(R.styleable.SelectOneImageView_addImage, R.drawable.ic_add_image)
        canPreView = ta.getBoolean(R.styleable.SelectOneImageView_canPreView, true) // 能发预览图片
        canDelete = ta.getBoolean(R.styleable.SelectOneImageView_canDelete,true) // 能否删除已选择的图片
        canPreViewDownload =
            ta.getBoolean(R.styleable.SelectOneImageView_canPreViewDownload, false) // 预览时能否长按下载
        canPreViewDelete =
            ta.getBoolean(R.styleable.SelectOneImageView_canPreViewDelete, true) // 预览时能否删除图片
        isCenterCrop = ta.getBoolean(R.styleable.SelectOneImageView_isCenterCrop, false)
        autoShowImg = ta.getBoolean(R.styleable.SelectOneImageView_autoShowImg,true)
        ta.recycle()
    }

    /**
     * 显示已选择的图片
     * imgUrl 为空时显示从本地选择的图片，imgUrl 不为空时 显示 imgUrl 中的图片，相当于回显
     */
    fun showSelImg(imgUrl:String? = null){
        if (imgUrl.getLength() > 0){
            if (canDelete) {
                iv_del.setVisible()
            }
            loadImage(fiv, imgUrl, isCenterCrop = isCenterCrop)
        }else{
            updateImg(true)
        }
    }

    private fun updateImg(showSelImg:Boolean = false) {
        showMedia?.let { media ->
            if (canDelete) {
                iv_del.setVisible()
            }
            if (autoShowImg || showSelImg) {
                loadImage(fiv, media.availablePath, isCenterCrop = isCenterCrop)
            }
        } ?: run {
            iv_del.setGone()
            fiv.setImageResource(addImage)
        }
    }

    lateinit var fiv: ImageView
    lateinit var iv_del: ImageView
    fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let { obtainAttributes(context, it) }
        val view = inflate(context, R.layout.view_select_one_image, this)

        fiv = view.findViewById<ImageView>(R.id.fiv)
        iv_del = view.findViewById<ImageView>(R.id.iv_del)
        iv_del.setImageResource(delImage)
        val tv_duration = view.findViewById<TextView>(R.id.tv_duration)
        tv_duration.setGone()
        updateImg()
        iv_del.click {
            if (canDelete) {
                delSelImg()
            }
        }
        fiv.click {
            if (showMedia == null) {
                PictureSelector.create(context)
                    .openGallery(openType)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .setImageEngine(getImageLoader())
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setSelectorUIStyle(WeChatStyle.createStyle(context))
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>) {
                            analyticalSelectResults(result)
                        }

                        override fun onCancel() {

                        }
                    })
            } else {
                if (!canPreView){
                    return@click
                }
                PictureSelector.create(context)
                    .openPreview()
                    .setImageEngine(getImageLoader())
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setSelectorUIStyle(PictureSelectorStyle())
                    .isHidePreviewDownload(!canPreViewDownload)
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        //外部预览监听事件
                        override fun onPreviewDelete(position: Int) {
                            showMedia = null
                            updateImg()
                        }

                        override fun onLongPressDownload(media: LocalMedia?): Boolean {
                            return false
                        }
                    })
                    .startActivityPreview(0, canPreViewDelete, arrayListOf(showMedia))
            }
        }
    }


    /**
     * 处理选择结果
     *
     * @param result
     */
    private fun analyticalSelectResults(result: ArrayList<LocalMedia>) {
        for (media in result) {
            if (media.width == 0 || media.height == 0) {
                if (PictureMimeType.isHasImage(media.mimeType)) {
                    val imageExtraInfo = MediaUtils.getImageSize(
                        context, media.path
                    )
                    media.width = imageExtraInfo.width
                    media.height = imageExtraInfo.height
                } else if (PictureMimeType.isHasVideo(media.mimeType)) {
                    val videoExtraInfo = MediaUtils.getVideoSize(
                        context, media.path
                    )
                    media.width = videoExtraInfo.width
                    media.height = videoExtraInfo.height
                }
            }
            logi("文件名: " + media.fileName)
            logi("是否压缩:" + media.isCompressed)
            logi("压缩:" + media.compressPath)
            logi("初始路径:" + media.path)
            logi("绝对路径:" + media.realPath)
            logi("是否裁剪:" + media.isCut)
            logi("裁剪路径:" + media.cutPath)
            logi("是否开启原图:" + media.isOriginal)
            logi("原图路径:" + media.originalPath)
            logi("沙盒路径:" + media.sandboxPath)
            logi("水印路径:" + media.watermarkPath)
            logi("视频缩略图:" + media.videoThumbnailPath)
            logi("原始宽高: " + media.width + "x" + media.height)
            logi("裁剪宽高: " + media.cropImageWidth + "x" + media.cropImageHeight)
            logi("文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.size))
            logi("文件时长: " + media.duration)
        }
        PictureThreadUtils.runOnUiThread {
            if (result.getSize() > 0) {
                showMedia = result[0]
                updateImg()
                showMedia?.let {
                    onSelectImgListener?.onSelect(it.getMyRealPath(context))
                }
            }
        }
    }

    /**
     * 删除已选择的图片
     */
    fun delSelImg(){
        showMedia = null
        updateImg()
    }

    var onSelectImgListener: OnSelectImgListener? = null

    interface OnSelectImgListener {
        fun onSelect(path:String)
    }

    fun getSelectMedia() = showMedia

    fun getSelectImgPath() = showMedia?.getMyRealPath(context)
}