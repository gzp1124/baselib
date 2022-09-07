package com.aligit.base.widget.select_image

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligit.base.R
import com.aligit.base.ext.getImageLoader
import com.aligit.base.ext.tool.logi
import com.blankj.utilcode.util.ConvertUtils
import com.luck.picture.lib.basic.*
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.*
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread
import com.luck.picture.lib.utils.MediaUtils
import com.luck.picture.lib.utils.PictureFileUtils
import java.util.*


/**
 * 从系统选择图片，选择九宫格图片
 * 一般用于，从系统选择上传九宫格图片
 xml 中使用
<com.aligit.base.widget.select_image.SelectImageView
android:id="@+id/sel2"
app:maxSelectNum="3"
app:spanCount="3"
app:addImage="@mipmap/ic_launcher"
android:layout_width="match_parent"
android:layout_height="wrap_content"/>

 代码中直接创建View
addLin.addView(
SelectImageView(requireContext(),3)
)
 */
class SelectImageView : RelativeLayout {

    private lateinit var mAdapter: GridImageAdapter
    private var maxSelectNum = 9
    private var spanCount = 4
    private var openType = SelectMimeType.ofAll()
    private var delImage: Int = 0
    private var addImage: Int = 0
    private var canPreView: Boolean = true // 能发预览图片
    private var canPreViewDownload: Boolean = false // 预览时能否长按下载
    private var canPreViewDelete: Boolean = true // 预览时能否删除图片

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
        maxSelectNum: Int = 9,
        spanCount: Int = 4,
        openType: Int = SelectMimeType.ofAll(),
        delImage: Int = 0,
        addImage: Int = 0,
        canPreView: Boolean = true,
        canPreViewDownload: Boolean = false,
        canPreViewDelete: Boolean = true
    ) : super(context) {
        this.maxSelectNum = maxSelectNum
        this.spanCount = spanCount
        this.openType = openType
        this.delImage = delImage
        this.addImage = addImage
        this.canPreView = canPreView
        this.canPreViewDownload = canPreViewDownload
        this.canPreViewDelete = canPreViewDelete
        init(context, null)
    }

    // xml 初始化 View
    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SelectImageView)
        maxSelectNum = ta.getInteger(R.styleable.SelectImageView_maxSelectNum, 9)
        spanCount = ta.getInteger(R.styleable.SelectImageView_spanCount, 4)
        openType = ta.getInteger(R.styleable.SelectImageView_openType, 0)
        delImage = ta.getResourceId(R.styleable.SelectImageView_delImage, R.drawable.ic_delete_menu)
        addImage = ta.getResourceId(R.styleable.SelectImageView_addImage, R.drawable.ic_add_image)
        canPreView = ta.getBoolean(R.styleable.SelectImageView_canPreView, true) // 能发预览图片
        canPreViewDownload =
            ta.getBoolean(R.styleable.SelectImageView_canPreViewDownload, false) // 预览时能否长按下载
        canPreViewDelete =
            ta.getBoolean(R.styleable.SelectImageView_canPreViewDelete, true) // 预览时能否删除图片
        ta.recycle()
    }

    fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let { obtainAttributes(context, it) }
        val view = inflate(context, R.layout.view_select_image, this)
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.id_recycler)

        val manager =
            FullyGridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = manager
        mRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                ConvertUtils.dp2px(8f),
                true
            )
        )
        mAdapter = GridImageAdapter(context, mutableListOf(), delImage, addImage)
        mAdapter.selectMax = maxSelectNum
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : GridImageAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                if (!canPreView) return
                // 预览图片、视频、音频
                PictureSelector.create(context)
                    .openPreview()
                    .setImageEngine(getImageLoader())
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setSelectorUIStyle(PictureSelectorStyle())
                    .isHidePreviewDownload(!canPreViewDownload)
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        //外部预览监听事件
                        override fun onPreviewDelete(position: Int) {
                            mAdapter.remove(position)
                            mAdapter.notifyItemRemoved(position)
                        }

                        override fun onLongPressDownload(media: LocalMedia?): Boolean {
                            return false
                        }
                    })
                    .startActivityPreview(position, canPreViewDelete, mAdapter.data)
            }

            override fun openPicture() {
                PictureSelector.create(context)
                    .openGallery(openType)
                    .isWithSelectVideoImage(true)
                    .setImageEngine(getImageLoader())
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setMaxSelectNum(maxSelectNum)
                    .setSelectorUIStyle(WeChatStyle.createStyle(context))
                    .setSelectedData(mAdapter.data)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>) {
                            analyticalSelectResults(result)
                        }

                        override fun onCancel() {

                        }
                    })
            }
        })
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
        runOnUiThread {
            val isMaxSize = result.size == mAdapter.selectMax
            val oldSize: Int = mAdapter.data.size
            mAdapter.notifyItemRangeRemoved(0, if (isMaxSize) oldSize + 1 else oldSize)
            mAdapter.data.clear()
            mAdapter.data.addAll(result)
            mAdapter.notifyItemRangeInserted(0, result.size)
        }
    }

    fun getAllSelImage(): ArrayList<LocalMedia>? {
        return mAdapter.data
    }

    fun getAllSelImagePath(): List<String>? {
        if (mAdapter.data == null) return null
        val res = mutableListOf<String>()
        mAdapter.data?.forEach { img ->
            res.add(img.availablePath)
        }
        return res
    }


}