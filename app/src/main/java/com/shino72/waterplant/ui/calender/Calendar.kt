package com.shino72.waterplant.ui.calender

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Calendar {
    private var cal = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    fun getDateList() : List<String> {
        cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -(getNw() - 1))
        println(dateFormat.format(cal.time))
        val rt = mutableListOf<String>(dateFormat.format(cal.time))
        cal.add(Calendar.DATE, 1)
        while (cal.get(Calendar.DAY_OF_WEEK) != 1)
        {
            rt.add(dateFormat.format(cal.time))
            cal.add(Calendar.DATE, 1)
        }
        return rt
    }
    fun getWeekStartDate() : String
    {
        cal = Calendar.getInstance()
        val nw = getNw()
        cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -nw + 1)
        return dateFormat.format(cal.time)
    }

    fun nowDate() : String {
        return dateFormat.format(cal.time)
    }
    private fun getNw() : Int {
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}