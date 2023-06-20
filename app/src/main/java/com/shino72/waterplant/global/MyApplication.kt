package com.shino72.waterplant.global

import android.annotation.SuppressLint
import android.app.Application
import android.icu.text.SimpleDateFormat
import java.util.*

class MyApplication : Application() {
    companion object {
        lateinit var pref : PreferenceUtil
    }

    override fun onCreate() {
        pref = PreferenceUtil(applicationContext)
        super.onCreate()
        getNowDate()
    }

    // 오늘 날짜를 찾음
    // prefs에 저장된 날짜와 다르면 -> 새로운 날이므로 마신 물 초기화
    // 같으면 -> 넘어가기
    private fun getNowDate()
    {
        val nowTime = getCurrentTime()
        if(nowTime != pref.getDate()){
            pref.setNow(0)
            pref.setDate(nowTime)
        }
    }

    fun getCurrentTime(): String{
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}