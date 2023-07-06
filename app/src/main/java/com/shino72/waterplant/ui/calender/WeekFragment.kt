package com.shino72.waterplant.ui.calender

import CustomBarChartRender
import android.annotation.SuppressLint
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
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
    private lateinit var calArray: MutableList<BarEntry>
    private lateinit var colorList: List<Int>
    private lateinit var dateList: MutableList<String>

    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeekBinding.inflate(inflater)
        calendar = Calendar()
        colorList = listOf(
            ContextCompat.getColor(requireContext(), R.color.cr_sun),
            ContextCompat.getColor(requireContext(), R.color.cr_mon),
            ContextCompat.getColor(requireContext(), R.color.cr_tue),
            ContextCompat.getColor(requireContext(), R.color.cr_wed),
            ContextCompat.getColor(requireContext(), R.color.cr_thu),
            ContextCompat.getColor(requireContext(), R.color.cr_fri),
            ContextCompat.getColor(requireContext(), R.color.cr_sat),
        )
        binding.apply {
            leftBtn.setOnClickListener {
                leftClick()
            }
            rightBtn.setOnClickListener {
                rightClick()
            }
        }
        calArray = mutableListOf()
        dateList = calendar.getWeekDateList() as MutableList<String>

        val customBarChartRender = CustomBarChartRender(
            binding.chartWeek,
            binding.chartWeek.animator,
            binding.chartWeek.viewPortHandler
        )
        binding.chartWeek.renderer = customBarChartRender

        initBar()
        return binding.root
    }

    private fun getDBallData(data : List<String>) {
        val appDataBase = com.shino72.waterplant.db.AppDataBase.getInstance(requireContext())
        dateList = mutableListOf()
        CoroutineScope(Dispatchers.Default).launch {
            val dao = appDataBase?.PlantDao()
            binding.drinkTv.text = data.startToEnd()
            dateList.addAll(data)
            calArray = mutableListOf()
            data.forEachIndexed { index, s ->
                val splitS = s.split("-")
                val rtc = dao?.getSingleCal(splitS[0], splitS[1], splitS[2])
                if (rtc != null) {
                    calArray.add(BarEntry(index.toFloat(), rtc.Size.toFloat()))
                } else calArray.add(BarEntry(index.toFloat(), 0f))
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

            binding.chartWeek.notifyDataSetChanged();
            binding.chartWeek.invalidate();
        }
    }

    private fun rightClick() {
        val nowDate = calendar.nowDate()
        val statusDate = binding.drinkTv.text.split(" ~ ")
        if (!(dateCompare(statusDate[1], nowDate))){
            calendar.setCal(statusDate[0])
            calendar.moveRightWeek()

            dateList = calendar.getWeek() as MutableList<String>

            binding.drinkTv.text = dateList.startToEnd()
            initBar()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun leftClick()
    {
        val statusDate = binding.drinkTv.text.split(" ~ ")

        calendar.setCal(statusDate[0])
        calendar.moveLeftWeek()

        dateList = calendar.getWeek() as MutableList<String>

        binding.drinkTv.text = dateList.startToEnd()
        initBar()
    }

    // d1 > d2이면 true 반대면 false
    private fun dateCompare(d1 : String, d2 : String) : Boolean
    {
        val d1s = d1.split("-").map { it.toInt() }
        val d2s = d2.split("-").map { it.toInt() }

        when
        {
            d1s[0] > d2s[0] -> return true
            d1s[0] < d2s[0] -> return false
            d1s[1] > d2s[1] -> return true
            d1s[1] < d2s[1] -> return false
            d1s[2] > d1s[2] -> return true
            d1s[2] < d2s[2] -> return false
            else -> return true
        }
    }
    private fun List<String>.startToEnd() : String {
        return if(this.size >= 2) this.first() +" ~ " +  this.last() else ""
    }

    private fun initBar()
    {
        // 마커 설정
        getDBallData(dateList)
        setCustomMarkerView()

        binding.chartWeek.run {
            setDrawBarShadow(true) // 그래프 그림자
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
                textSize = 11f // 라벨 크기
                textColor = Color.BLACK
                yOffset = 2f
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
    }

    private fun setCustomMarkerView()
    {
        val markerView= CustomMarkerView(requireContext(), layout = R.layout.marker_view, dateList)
        markerView.chartView = binding.chartWeek
        binding.chartWeek.marker = markerView
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