package com.shino72.waterplant.ui.calender


import android.annotation.SuppressLint
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

import com.shino72.waterplant.databinding.FragmentMonthBinding
import com.shino72.waterplant.db.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MonthFragment : Fragment() {

    private lateinit var binding: FragmentMonthBinding
    private lateinit var appDataBase: AppDataBase
    private lateinit var dateList: MutableList<String>
    private lateinit var colorList: List<Int>
    private lateinit var calendar: Calendar
    private lateinit var monthData: MutableList<Pair<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthBinding.inflate(inflater)
        dateList = mutableListOf()
        appDataBase = AppDataBase.getInstance(requireContext())!!
        calendar = Calendar()
        monthData = mutableListOf()
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
        val nd = calendar.nowDate().split("-").map { it.toInt() }
        getWeek(nd[0],nd[1]-1)
        initBar()
        return binding.root
    }

    private fun getWeek(year : Int, month : Int) {
        monthData = mutableListOf()
        // 일단 캘린더의 날짜를 해당 년 월의 1일로 설정
        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        // 말일 구하기
        val lastDay = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        val lastCal = java.util.Calendar.getInstance()
        lastCal.set(year, month, lastDay)
        // 해당 요일 구하기
        val d = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        println(d)
        val df = dateFormat.format(calendar.time)
        calendar.add(java.util.Calendar.DATE, (7 - d))

        monthData.add(Pair(df, dateFormat.format(calendar.time)))

        calendar.add(java.util.Calendar.DATE, 1)
        while(true)
        {
            val df = dateFormat.format(calendar.time)
            calendar.add(java.util.Calendar.DATE, 7)
            if(calendar.time < lastCal.time)
            {
                calendar.add(java.util.Calendar.DATE, -1)
                monthData.add(Pair(df, dateFormat.format(calendar.time)))
                calendar.add(java.util.Calendar.DATE, 1)
            }
            else
            {
                val s = df.split("-")
                monthData.add(Pair(df, dateFormat.format(lastCal.time)))
                break
            }
        }
        binding.drinkTv.text = monthData.first().first + " ~ " + monthData.last().second
    }


    private fun rightClick() {
        calendar.moveRighttMonth()
        val d = calendar.getCalDateFormat().split("-").map { it.toInt() }
        getWeek(d[0],d[1]-1)
        initBar()
    }

    @SuppressLint("SetTextI18n")
    private fun leftClick()
    {
        calendar.moveLeftMonth()
        val d = calendar.getCalDateFormat().split("-").map { it.toInt() }
        getWeek(d[0],d[1]-1)
        initBar()
    }
    private fun getDBallData(data : List<Pair<String,String>>) {
        println(data)
        CoroutineScope(Dispatchers.Default).launch {
            val dao = appDataBase.PlantDao()
            val calArray = mutableListOf<BarEntry>()
            data.forEachIndexed { index, s ->
                val first = s.first
                val second = s.second
                val s1 = first.split("-")
                val s2 = second.split("-")
                val size = dao.getWeekSize(s1[0], s1[1], s1[2], s2[2])
                if(size != null) calArray.add(BarEntry(index.toFloat(),size.toFloat()))
                else calArray.add(BarEntry(index.toFloat(), 0f))

            }
            val set = BarDataSet(calArray, "내용없음")
                .apply {
                    valueFormatter = YearFragment.WaterCustomFormatter()
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

    private fun setCustomMarkerView()
    {
        val n = monthData.map { it.first + "\n" + it.second}
        val markerView= CustomMarkerView(requireContext(), layout = R.layout.marker_view, n as MutableList<String>)
        markerView.chartView = binding.chartWeek
        binding.chartWeek.marker = markerView
    }
    private fun initBar()
    {
        // 마커 설정
        getDBallData(monthData)
        setCustomMarkerView()

        binding.chartWeek.run {
            setDrawBarShadow(true) // 그래프 그림자
            setDrawValueAboveBar(true) // 입력?값이 차트 위or아래에 그려질 건지 (true=위, false=아래)
            setPinchZoom(false) // 두손가락으로 줌 설정
            setDrawGridBackground(false) // 격자구조
            setMaxVisibleValueCount(12) // 그래프 최대 갯수
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
                setScaleEnabled(false)

            }

            axisLeft.run { // 왼쪽 y축
                isEnabled = false
                axisMinimum = 0f // 최소값
                axisMaximum = 30000f // 최대값
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
    class LabelCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return (value.toInt()+1).toString() + "주차"
        }

        override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry?): String {
            return super.getBarStackedLabel(value, stackedEntry)
        }
    }

    class WaterCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString().split(".")[0]+"ml"
        }
    }
}
