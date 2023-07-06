package com.shino72.waterplant.ui.calender


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.shino72.waterplant.R

import com.shino72.waterplant.databinding.FragmentMonthBinding
import com.shino72.waterplant.db.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MonthFragment : Fragment() {

    private lateinit var binding: FragmentMonthBinding
    private lateinit var appDataBase: AppDataBase
    private lateinit var dateList: List<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthBinding.inflate(inflater)
        dateList = listOf()
        appDataBase = AppDataBase.getInstance(requireContext())!!

        CoroutineScope(Dispatchers.Default).launch {
            println(appDataBase.PlantDao().getMonthSingle("2023","07"))
        }

        return binding.root
    }
}