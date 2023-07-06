package com.shino72.waterplant.ui.calender

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Calendar {
    private var cal = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    fun getWeekDateList() : List<String> {
        cal.add(Calendar.DATE, -(getNw() - 1))
        val tmpCal = cal
        println(dateFormat.format(cal.time))
        val rt = mutableListOf<String>(dateFormat.format(cal.time))
        cal.add(Calendar.DATE, 1)
        while (cal.get(Calendar.DAY_OF_WEEK) != 1)
        {
            rt.add(dateFormat.format(cal.time))
            cal.add(Calendar.DATE, 1)
        }
        cal = tmpCal
        return rt
    }

    fun moveLeftWeek()
    {
        cal.add(Calendar.DATE, -7)
    }

    fun moveRightWeek()
    {
        cal.add(Calendar.DATE ,7)
    }

    fun setCal(s : String)
    {
        val sp = s.split("-").map { it.toInt() }
        cal.set(
            sp[0],sp[1]-1 , sp[2]
        )
    }
    fun getWeek() : List<String>
    {
        val tmpCal = cal
        val l = mutableListOf<String>()
        repeat(7)
        {
            l.add(dateFormat.format(cal.time))
            cal.add(Calendar.DATE, 1)
        }
        cal= tmpCal
        return l
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
        return dateFormat.format(Calendar.getInstance().time)
    }
    private fun getNw() : Int {
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}