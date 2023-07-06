package com.shino72.waterplant.ui.calender

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.FragmentWeekBinding
import com.shino72.waterplant.databinding.FragmentYearBinding
import com.shino72.waterplant.db.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YearFragment : Fragment() {
    private lateinit var binding : FragmentYearBinding
    private lateinit var appDataBase: AppDataBase
    private lateinit var colorList: List<Int>
    private lateinit var monthData : MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYearBinding.inflate(inflater)
        appDataBase = AppDataBase.getInstance(requireContext())!!
        colorList = listOf(
            ContextCompat.getColor(requireContext(), R.color.cr_sun),
            ContextCompat.getColor(requireContext(), R.color.cr_mon),
            ContextCompat.getColor(requireContext(), R.color.cr_tue),
            ContextCompat.getColor(requireContext(), R.color.cr_wed),
            ContextCompat.getColor(requireContext(), R.color.cr_thu),
            ContextCompat.getColor(requireContext(), R.color.cr_fri),
            ContextCompat.getColor(requireContext(), R.color.cr_sat),

        )

        monthData = mutableListOf("01","02","03","04","05","06","07","08","09","10","11","12")
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()
        monthData = monthData.map {
            "${year}-${it}"
        } as MutableList<String>

        binding.apply {
            drinkTv.text = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()
            leftBtn.setOnClickListener {
                leftClick()
                binding.chartWeek.fitScreen()
                binding.chartWeek.zoom(1.5f,0f,0f,0f)
            }
            rightBtn.setOnClickListener {
                rightClick()
                binding.chartWeek.fitScreen()
                binding.chartWeek.zoom(1.5f,0f,0f,0f)
            }
        }
        initBar()

        return binding.root
    }

    private fun rightClick() {
        val statusYear = (binding.drinkTv.text.toString().toInt() + 1).toString()

        monthData = monthData.map {
            statusYear+"-"+it.split("-")[1]
        } as MutableList<String>
        println(monthData)
        binding.drinkTv.text = statusYear
        initBar()
    }

    @SuppressLint("SetTextI18n")
    private fun leftClick()
    {
        val statusYear = (binding.drinkTv.text.toString().toInt() - 1).toString()

        monthData = monthData.map {
            statusYear+"-"+it.split("-")[1]
        } as MutableList<String>
        println(monthData)
        binding.drinkTv.text = statusYear
        initBar()
    }
    private fun getDBallData(data : List<String>) {
        println(data)
        CoroutineScope(Dispatchers.Default).launch {
            val dao = appDataBase.PlantDao()
            val calArray = mutableListOf<BarEntry>()
            data.forEachIndexed { index, s ->
                val splitS = s.split("-")
                val rtc = dao.getMonthSingle(splitS[0], splitS[1])
                if(rtc != null) calArray.add(BarEntry(index.toFloat(), rtc.toFloat()))
                else calArray.add(BarEntry(index.toFloat(), 0f))
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

    private fun setCustomMarkerView()
    {
        val markerView= CustomMarkerView(requireContext(), layout = R.layout.marker_view, monthData as MutableList<String>)
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
                //setScaleEnabled(false)
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
            fitScreen()
            zoom(1.5f,0f,0f,0f)
            animateY(1500) // y축 애니메이션
            animateX(1000) // x축 애니메이션
        }
    }
    class LabelCustomFormatter : ValueFormatter() {
        private var xLabels = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep","Oct","Nov","Dec"
        )
        override fun getFormattedValue(value: Float): String {
            return xLabels.get(value.toInt())
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