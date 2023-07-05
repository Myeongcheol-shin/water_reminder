package com.shino72.waterplant.ui.calender

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Calendar {
    private var cal = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val nw = cal.get(Calendar.DAY_OF_WEEK)

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
    fun getNw() : Int {
        return nw
    }

}