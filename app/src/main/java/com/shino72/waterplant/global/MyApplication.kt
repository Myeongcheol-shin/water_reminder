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
import kotlinx.coroutines.CoroutineScope
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
    // prefs에 저장된 날짜와 다르면 -> 새로운 날이므로 마신 물 초기화 + 기존에 저장된 데이터를 해당 날짜에 저장
    // 같으면 -> 넘어가기
    private fun getNowDate() {
        val nowTime = pref.getDate()
        val appDataBase = AppDataBase.getInstance(applicationContext)
        if (nowTime != null && nowTime != pref.getDate()) {
            val nowDate = nowTime.dateSubstring()
            if(nowDate.size != 3) return
            CoroutineScope(Dispatchers.Default).launch {
                val data = appDataBase!!.PlantDao().getSingleCal(nowDate[0], nowDate[1], nowDate[2])
                if(data == null){
                    appDataBase!!.PlantDao().insertWaterAll(com.shino72.waterplant.db.Calendar(Year = nowDate[0].toInt(), Month = nowDate[1].toInt(), Day = nowDate[2].toInt(), pref.getNow()))
                }
                else{
                    appDataBase!!.PlantDao().updateCal(com.shino72.waterplant.db.Calendar(Year = nowDate[0].toInt(), Month = nowDate[1].toInt(), Day = nowDate[2].toInt(), pref.getNow()))
                }
            }
            pref.setNow(0)
            pref.setDate(nowTime)
        }
    }
    fun String.dateSubstring() : List<String> {
        return listOf(
            this.substring(0 until 5),
            this.substring(5 until 7),
            this.substring(7..8)
        )
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
                    name = "해봐라기",
                    image1 = (getDrawable(R.drawable.icon_seed) as BitmapDrawable).bitmap,
                    image2 = (getDrawable(R.drawable.icon_plant) as BitmapDrawable).bitmap,
                    image3 = (getDrawable(R.drawable.icon_not_rose) as BitmapDrawable).bitmap,
                    image4 = (getDrawable(R.drawable.icon_rose) as BitmapDrawable).bitmap
                )
            )
            pref.setUpdateStatus(status = true)
        }
    }

}