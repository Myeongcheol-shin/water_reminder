package com.shino72.waterplant.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shino72.waterplant.db.AppDataBase
import com.shino72.waterplant.global.MyApplication

class MainViewModel : ViewModel() {
    private val _now = MutableLiveData<Int>()
    private val _goal = MutableLiveData<Int>()
    private val _name = MutableLiveData<String>()
    private val _wave = MutableLiveData<Int>()
    private val _progress = MutableLiveData<Int>()

    val now: LiveData<Int> get() = _now
    val goal: LiveData<Int> get() = _goal
    val name: LiveData<String> get() = _name
    val wave: LiveData<Int> get() = _wave
    val progress : LiveData<Int> get() = _progress

    fun valueChange()
    {
        _now.value = MyApplication.pref.getNow()
        _goal.value = MyApplication.pref.getGoal()
        _name.value = MyApplication.pref.getName()
        _progress.value = if(_now.value == 0) 0 else (_now.value!! * 100) / _goal.value!!
        when (_progress.value) {
            in (0 until 30) -> {
                _wave.value = 1
            }
            in (30 until 70) -> {
                _wave.value = 2
            }
            in (70 until 100) -> {
                _wave.value = 3
            }
            else -> {
                _wave.value = 4
            }
        }
    }

    init {
        valueChange()
    }
}