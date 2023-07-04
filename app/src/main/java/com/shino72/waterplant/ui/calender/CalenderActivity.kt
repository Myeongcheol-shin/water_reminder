package com.shino72.waterplant.ui.calender

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.slider.LabelFormatter
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.ActivityCalenderBinding
import java.util.*


class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private var selectedBtn = 0
    private lateinit var itemButtonList: List<CalView>

    private data class CalView(val cv : CardView, val ll : LinearLayout, val ib : ImageButton, val tv: TextView)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBar()

        itemButtonList = listOf(
            CalView(binding.weekCv, binding.weekLl, binding.weekBtn, binding.weekTv),
            CalView(binding.monthCv,binding.monthLl, binding.monthBtn, binding.monthTv),
            CalView(binding.yearCv, binding.yearLl, binding.yearBtn, binding.yearTv)
        )

        binding.apply {
            itemButtonList.forEachIndexed { index, cv ->
                cv.ib.setOnClickListener {
                    setBtnClick(index)
                }
            }
        }
    }


    fun initBar()
    {
        binding.chartWeek.run {

            setDrawBarShadow(true) // 그래프 그림자
            setTouchEnabled(false) // 차트 터치 막기
            setDrawValueAboveBar(true) // 입력?값이 차트 위or아래에 그려질 건지 (true=위, false=아래)
            setPinchZoom(false) // 두손가락으로 줌 설정
            setDrawGridBackground(false) // 격자구조
            setMaxVisibleValueCount(10) // 그래프 최대 갯수
            description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
            legend.isEnabled = false // 차트 범례 설정(legend object chart)

            xAxis.run { // 아래 라벨 x축
                isEnabled = true // 라벨 표시 설정
                valueFormatter = LabelCustomFormatter()

                position = XAxis.XAxisPosition.BOTTOM // 라벨 위치 설정
                setDrawGridLines(false) // 격자구조
                granularity = 1f // 간격 설정
                setDrawAxisLine(false) // 그림
                textSize = 12f // 라벨 크기
                textColor = Color.BLACK
            }

            axisLeft.run { // 왼쪽 y축
                isEnabled = false
                axisMinimum = 0f // 최소값
                axisMaximum = 5000f // 최대값
                granularity = 100f // 값 만큼 라인선 설정
                setDrawLabels(false) // 값 셋팅 설정
                textColor = Color.RED // 색상 설정
                axisLineColor = Color.BLACK // 축 색상 설정
                gridColor = Color.BLUE // 격자 색상 설정
            }
            axisRight.run { // 오른쪽 y축(왼쪽과동일)
                isEnabled = false
                textSize = 15f
            }

            animateY(1500) // y축 애니메이션
            animateX(1000) // x축 애니메이션
        }
        val values = mutableListOf<BarEntry>()

        val colorList = listOf(
            ContextCompat.getColor(applicationContext, R.color.cr_sun),
            ContextCompat.getColor(applicationContext, R.color.cr_mon),
            ContextCompat.getColor(applicationContext, R.color.cr_tue),
            ContextCompat.getColor(applicationContext, R.color.cr_wed),
            ContextCompat.getColor(applicationContext, R.color.cr_thu),
            ContextCompat.getColor(applicationContext, R.color.cr_fri),
            ContextCompat.getColor(applicationContext, R.color.cr_sat),
        )

        for (i in 0..6) {
            val index = i.toFloat()
            val random = Random().nextInt(101).toFloat()

            values.add(BarEntry(index, random))
        }

        val set = BarDataSet(values, "내용없음")
            .apply {
                valueFormatter = ScoreCustomFormatter()
                setDrawIcons(false)
                setDrawValues(true)
                colors = colorList
                valueTextColor = Color.BLACK
            }

        val dataSets = mutableListOf<IBarDataSet>()
        dataSets.add(set)

        val data = BarData(dataSets)
            .apply {
                setValueTextSize(10f)
                barWidth = 0.5f
            }

        binding.chartWeek.data = data
    }
    class LabelCustomFormatter : ValueFormatter() {
        private var index = 0

        override fun getFormattedValue(value: Float): String {
            index = value.toInt()
            return when (index) {
                0 -> "Sun"
                1 -> "Mon"
                2 -> "Tue"
                3 -> "Wed"
                4 -> "Thu"
                5 -> "Fri"
                6 -> "Sat"
                else -> throw IndexOutOfBoundsException("index out")
            }
        }

        override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry?): String {
            return super.getBarStackedLabel(value, stackedEntry)
        }
    }

    class ScoreCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val score = value.toString().split(".")
            return score[0]+"ml"
        }
    }

    fun setBtnClick(item : Int)
    {
        itemButtonList[selectedBtn].tv.visibility = View.GONE
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(
            com.shino72.waterplant.R.color.white))

        val param1 = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        val mDp  : Int = (binding.root.resources.displayMetrics.density * 10).toInt()
        param1.setMargins(
            mDp,mDp,mDp,mDp
        )
        itemButtonList[selectedBtn].ll.setBackgroundColor(android.graphics.Color.BLACK)


        itemButtonList[selectedBtn].cv.layoutParams = param1

        val param2 = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        param2.setMargins(
            mDp,mDp,mDp,mDp
        )

        selectedBtn = item
        itemButtonList[selectedBtn].tv.visibility = View.VISIBLE
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(
            com.shino72.waterplant.R.color.black))

        val showAnimation = AlphaAnimation(0f, 1f)
        showAnimation.duration = 1000

        itemButtonList[selectedBtn].ll.background = ContextCompat.getDrawable(this, com.shino72.waterplant.R.drawable.gradation_green)


        itemButtonList[selectedBtn].tv.animation = showAnimation
        param2.weight = 1f
        itemButtonList[selectedBtn].cv.layoutParams = param2
    }
}