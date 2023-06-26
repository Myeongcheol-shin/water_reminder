package com.shino72.waterplant.global

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)

    fun getName() : String? {
        return prefs.getString("name", null)
    }
    fun setName(name : String) {
        prefs.edit().putString("name",name).apply()
    }
    fun getNow() : Int {
        return prefs.getInt("now",0)
    }
    fun setNow(now : Int) {
        prefs.edit().putInt("now",now).apply()
    }
    fun getGoal() : Int {
        return prefs.getInt("goal",0)
    }
    fun setGoal(goal : Int){
        prefs.edit().putInt("goal",goal).apply()
    }

    fun getDate() : String?
    {
        return prefs.getString("date","")
    }
    fun setDate(date : String)
    {
        prefs.edit().putString("date", date).apply()
    }

    fun getUpdateStatus() : Boolean
    {
        return prefs.getBoolean("status",false)
    }
    fun setUpdateStatus(status : Boolean)
    {
        prefs.edit().putBoolean("status", status).apply()
    }

    fun setSelectedId(id : Int)
    {
        prefs.edit().putInt("id",id).apply()
    }
    fun getSelectedId() : Int
    {
        return prefs.getInt("id",1)
    }
}