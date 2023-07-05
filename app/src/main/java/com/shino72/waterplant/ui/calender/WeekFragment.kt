package com.shino72.waterplant.ui.calender

import CustomBarChartRender
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.FragmentWeekBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeekFragment : Fragment() {
    private lateinit var binding: FragmentWeekBinding
    private lateinit var calArray : MutableList<BarEntry>
    private lateinit var colorList : List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeekBinding.inflate(inflater)
        colorList = listOf(
            ContextCompat.getColor(requireContext(), R.color.cr_sun),
            ContextCompat.getColor(requireContext(), R.color.cr_mon),
            ContextCompat.getColor(requireContext(), R.color.cr_tue),
            ContextCompat.getColor(requireContext(), R.color.cr_wed),
            ContextCompat.getColor(requireContext(), R.color.cr_thu),
            ContextCompat.getColor(requireContext(), R.color.cr_fri),
            ContextCompat.getColor(requireContext(), R.color.cr_sat),
        )
        calArray = mutableListOf()

        val customBarChartRender = CustomBarChartRender(binding.chartWeek,binding.chartWeek.animator,binding.chartWeek.viewPortHandler)
        binding.chartWeek.renderer = customBarChartRender

        initBar()
        return binding.root
    }

    fun getDBallData()
    {
        val appDataBase = com.shino72.waterplant.db.AppDataBase.getInstance(requireContext())
        CoroutineScope(Dispatchers.Default).launch {
            val dao = appDataBase?.PlantDao()
            val data = Calendar().getDateList()
            data.forEachIndexed { index, s ->
                val splitS = s.split("-")
                val rtc = dao?.getSingleCal(splitS[0], splitS[1], splitS[2])
                if (rtc != null) {
                    calArray.add(BarEntry(index.toFloat(), rtc.Size.toFloat()))
                }
                else calArray.add(BarEntry(index.toFloat(), 0f))
            }
            while (calArray.size < 7){
                calArray.add(BarEntry(calArray.size.toFloat(), 0f))
            }

            val set = BarDataSet(calArray, "내용없음")
                .apply {
                    valueFormatter = WaterCustomFormatter()
                    setDrawIcons(false)
                    setDrawValues(true)
                    colors = colorList
                    valueTextColor = Color.BLACK
                }

            val dataSets = mutableListOf<IBarDataSet>()
            dataSets.add(set)

            val datas = BarData(dataSets)
                .apply {
                    setValueTextSize(10f)
                    barWidth = 0.5f
                }

            binding.chartWeek.data = datas
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

        getDBallData()

    }
    class LabelCustomFormatter : ValueFormatter() {
        private var index = 0

        override fun getFormattedValue(value: Float): String {
            index = value.toInt()
            return when (index) {
                0 -> "일"
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                6 -> "토"
                else -> throw IndexOutOfBoundsException("index out")
            }
        }

        override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry?): String {
            return super.getBarStackedLabel(value, stackedEntry)
        }
    }

    class WaterCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val score = value.toString().split(".")
            return score[0]+"ml"
        }
    }
}