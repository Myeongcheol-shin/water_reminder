package com.shino72.waterplant.ui.calender

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.shino72.waterplant.R
import java.lang.IndexOutOfBoundsException

class CustomMarkerView(context : Context, layout : Int, private val datas : MutableList<String>) : MarkerView(context, layout) {
    private var tvContent : TextView = findViewById(R.id.tvContentHead)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val barEntry = e as BarEntry
        try{
            val xAxis = barEntry.x.toInt() ?: 0
            tvContent.text = datas[xAxis]
        }
        catch (e : IndexOutOfBoundsException) {}

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}