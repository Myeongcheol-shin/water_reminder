package com.shino72.waterplant.global

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.provider.Settings.Global
import com.shino72.waterplant.Adapter.Plant
import com.shino72.waterplant.R
import com.shino72.waterplant.db.AppDataBase
import com.shino72.waterplant.db.PlantPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MyApplication : Application() {
    companion object {
        lateinit var pref: PreferenceUtil
    }

    override fun onCreate() {
        pref = PreferenceUtil(applicationContext)
        super.onCreate()
        getNowDate()
        GlobalScope.launch {
            setData()
        }
    }

    // 오늘 날짜를 찾음
    // prefs에 저장된 날짜와 다르면 -> 새로운 날이므로 마신 물 초기화
    // 같으면 -> 넘어가기
    private fun getNowDate() {
        val nowTime = getCurrentTime()
        if (nowTime != pref.getDate()) {
            pref.setNow(0)
            pref.setDate(nowTime)
        }
    }

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    // 식물 데이터 저장하기
    @SuppressLint("UseCompatLoadingForDrawables")
    private suspend fun setData() {
        if (!pref.getUpdateStatus()) {
            val db = AppDataBase.getInstance(this)
            db!!.PlantDao().deleteAll()
            db!!.PlantDao().insertDB(
                PlantPicture(
                    name = "꼬치",
                    image1 = (getDrawable(R.drawable.icon_seed) as BitmapDrawable).bitmap,
                    image2 = (getDrawable(R.drawable.icon_plant) as BitmapDrawable).bitmap,
                    image3 = (getDrawable(R.drawable.icon_not_rose) as BitmapDrawable).bitmap,
                    image4 = (getDrawable(R.drawable.icon_rose) as BitmapDrawable).bitmap
                )
            )
            db!!.PlantDao().insertDB(
                PlantPicture(
                    name = "꼬치2",
                    image2 = (getDrawable(R.drawable.icon_seed) as BitmapDrawable).bitmap,
                    image1 = (getDrawable(R.drawable.icon_plant) as BitmapDrawable).bitmap,
                    image3 = (getDrawable(R.drawable.icon_not_rose) as BitmapDrawable).bitmap,
                    image4 = (getDrawable(R.drawable.icon_rose) as BitmapDrawable).bitmap
                )
            )
            pref.setUpdateStatus(status = true)
        }
    }

}