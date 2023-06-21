package com.shino72.waterplant.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.shino72.waterplant.global.MyApplication

class MainViewModel : ViewModel() {
    private val _now = MutableLiveData<Int>()
    private val _goal = MutableLiveData<Int>()
    private val _name = MutableLiveData<String>()
    private val _wave = MutableLiveData<Int>()

    val now: LiveData<Int> get() = _now
    val goal: LiveData<Int> get() = _goal
    val name: LiveData<String> get() = _name
    val wave: LiveData<Int> get() = _wave

    fun valueChange()
    {
        _now.value = MyApplication.pref.getNow()
        _goal.value = MyApplication.pref.getGoal()
        _name.value = MyApplication.pref.getName()
        val progress = if(_now.value == 0) 0 else (_now.value!! * 100) / _goal.value!!
        _wave.value = if(progress > 100) 100 else progress
    }

    init {
        valueChange()
    }
}