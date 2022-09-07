package com.aligit.base.widget.show_grid_image

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligit.base.R
import com.aligit.base.ext.getImageLoader
import com.aligit.base.ext.startShowImageFragment
import com.aligit.base.ext.tool.toast
import com.aligit.base.widget.select_image.ExoPlayerEngine
import com.aligit.base.widget.select_image.FullyGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.style.PictureSelectorStyle

/**
 * 显示九宫格图片
 * 一般用来网络请求回来的图片，显示成九宫格
 * xml 和 代码 中都能用

<com.aligit.base.widget.show_grid_image.ShowGridImageView
android:id="@+id/onlyShow"
android:layout_width="match_parent"
android:layout_height="wrap_content"/>
 然后在代码中设置数据：onlyShow.showImgs(imgs)

 代码中直接创建View：
showLin.addView(
ShowGridImageView(requireContext(), spanCount = 3).apply {
showImgs(imgs)
}
)
 */
class ShowGridImageView: RelativeLayout {
    private lateinit var mAdapter: ShowGridImageAdapter
    private var spanCount = 4
    private var canPreView: Boolean = true // 能发预览图片
    private var canPreViewDownload: Boolean = false // 预览时能否长按下载
    
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
        spanCount: Int = 4,
        canPreView: Boolean = true,
        canPreViewDownload: Boolean = false
    ) : super(context) {
        this.spanCount = spanCount
        this.canPreView = canPreView
        this.canPreViewDownload = canPreViewDownload
        init(context, null)
    }

    // xml 初始化 View
    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShowGridImageView)
        spanCount = ta.getInteger(R.styleable.ShowGridImageView_spanCount, 4)
        canPreView = ta.getBoolean(R.styleable.ShowGridImageView_canPreView, true) // 能发预览图片
        canPreViewDownload =
            ta.getBoolean(R.styleable.ShowGridImageView_canPreViewDownload, false) // 预览时能否长按下载
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
        mAdapter = ShowGridImageAdapter()
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startShowImageFragment(mAdapter.data, currentIndex = position)
        }
    }

    fun showImgs(list:List<String>){
        mAdapter.setList(list)
    }
}