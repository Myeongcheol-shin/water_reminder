package com.shino72.waterplant.ui.calender


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shino72.waterplant.databinding.FragmentMonthBinding

class MonthFragment : Fragment() {

    private lateinit var binding: FragmentMonthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthBinding.inflate(inflater)
        return binding.root
    }

}