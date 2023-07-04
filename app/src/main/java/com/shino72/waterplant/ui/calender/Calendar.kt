package com.shino72.waterplant.ui.calender

import com.bumptech.glide.Glide.get
import com.bumptech.glide.Glide.init
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class Calendar {
    private var cal = java.util.Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val nw = cal.get(java.util.Calendar.DAY_OF_WEEK)

    fun getDateList() : List<String> {
        cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DATE, getNw() - 1)

        val rt = mutableListOf<String>()
        repeat(7)
        {
            rt.add(dateFormat.format(cal.time))
        }
        return rt
    }
    fun getNw() : Int {
        return nw
    }

}