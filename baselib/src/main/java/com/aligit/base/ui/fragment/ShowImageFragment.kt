package com.aligit.base.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.R
import com.aligit.base.databinding.FragmentShowImageBinding
import com.aligit.base.ext.tool.toast
import com.aligit.base.ext.view.click
import com.aligit.base.model.ShowImageBean
import com.bumptech.glide.Glide
import com.luck.picture.lib.photoview.PhotoView

/**
 * 左右滑动展示多张图片/视频
 */
@Route(path = "/common/showimage")
class ShowImageFragment : BaseVmFragment<FragmentShowImageBinding>(R.layout.fragment_show_image) {

    val list: MutableList<View> = mutableListOf()

    override fun onInitDataBinding() {
        val urls = arguments?.getParcelableArrayList<ShowImageBean>("showimage")
        val currentIndex = arguments?.getInt("currentIndex",0) ?: 0
        if (urls == null || urls.size == 0) {
            toast { "urls is empty" }
            activity?.finish()
            return
        }
        mDataBinding.tvCount.text = " / ${urls.size}"

        for (i in urls.indices) {
            val view: View = layoutInflater.inflate(R.layout.item_big_image, null)
            val mIvShow = view.findViewById<PhotoView>(R.id.mIvShow)
            Glide.with(this).load(urls[i].mUrl).into(mIvShow)
            mIvShow.click { activity?.finish() }
            list.add(view)
        }

        mDataBinding.mIvShow.setAdapter(ViewPagerAdapter(list))
        mDataBinding.mIvShow.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mDataBinding.tvIndex.text = "${position + 1}"
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mDataBinding.mIvShow.setCurrentItem(currentIndex,false)
    }


    internal class ViewPagerAdapter(var list: List<View>) :
        PagerAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(list[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(list[position])
            return list[position]
        }
    }
}