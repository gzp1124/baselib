package com.thirtydays.baselibdev.ui

import android.graphics.Color
import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ui.fragment.BaseVmFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestMpChartBinding
import kotlin.random.Random


/*
测试使用
参考文档：https://blog.csdn.net/u014136472/article/details/50273309
文档2：https://svipbug.com/2020/03/19/MPAndroidChart的详细使用——坐标轴（X轴、Y轴、零线）/

 */
@Route(path = "/test/mp_chart")
class TestMpChartFragment:BaseVmFragment<FragmentTestMpChartBinding>(R.layout.fragment_test_mp_chart) {

    val random = Random(100)

    override fun onInitDataBinding() {
        loadHorizontalBarData()
    }

    // 水平柱状图
    private fun loadHorizontalBarData(){
        // 图表设置
        mDataBinding.chart.run {
            // 图例 Legend 是否显示，显示在底部，表示各个线代表什么 BarDataSet 的 label
            legend.isEnabled = false
            // 右下角的描述是否显示
            description.isEnabled = false
            // 图表中的值显示在 bar 的下方
            setDrawValueAboveBar(false)
            // 图表是否可触摸
            setTouchEnabled(false)

            // x轴设置
            xAxis.run {
                // x轴的显示位置
                position = XAxis.XAxisPosition.BOTTOM
                // 网格线
                setDrawGridLines(false)
                // 坐标值
                setDrawLabels(false)
            }

            // x轴的左侧坐标轴设置
            axisLeft.run {
                // 所有线都不显示
                isEnabled = false
            }

            // x轴的右侧坐标轴设置
            axisRight.run {
                // 不显示网格线
                setDrawGridLines(false)
            }
        }

        //所有数据点的集合
        val entries1 = arrayListOf<BarEntry>()
        for (i in 0..5){
            entries1.add(BarEntry(i.toFloat(),random.nextFloat()))
        }

        //柱形数据的集合
        val barDataSet = BarDataSet(entries1, "barDataSet1").apply {
            // 柱状体的颜色
            colors = arrayListOf(Color.parseColor("#FF592F99"))
            // 显示文字
            setDrawValues(true)
            // 文字颜色
            setValueTextColors(arrayListOf(Color.WHITE))
        }

        //BarData表示挣个柱形图的数据
        val barData = BarData()
        // 柱状图的宽度
        barData.barWidth = 0.2f
        barData.addDataSet(barDataSet)

        // 把数据设置到图标中
        mDataBinding.chart.data = barData
    }
}