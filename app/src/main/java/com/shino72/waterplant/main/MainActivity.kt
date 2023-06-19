package com.shino72.waterplant.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.ActivityMainBinding
import com.shino72.waterplant.dialog.SlideUpDialog

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var settingContentView : View
    private lateinit var settingSlideUpPopup : SlideUpDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.main = viewModel


        // slideView 동적 추가
        settingContentView = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_setting, null)
        settingSlideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(settingContentView)
            .create()

        initSlider()

        // binding
        binding.apply {
            settingBtn.setOnClickListener {
                settingSlideUpPopup.show()
            }
        }
    }

    private fun initSlider()
    {
        val cancelBtn = settingContentView.findViewById<AppCompatButton>(R.id.cancel_button)
        val applyBtn = settingContentView.findViewById<AppCompatButton>(R.id.apply_btn)

        cancelBtn.setOnClickListener {
            settingSlideUpPopup.dismissAnim()
        }

        applyBtn.setOnClickListener {

            val nowDrink = settingContentView.findViewById<EditText>(R.id.now_et).text.toString()
            val goalDrink = settingContentView.findViewById<EditText>(R.id.goal_et).text.toString()
            if(goalDrink == "0") Toast.makeText(this,"목표는 0이 될 수 없습니다",Toast.LENGTH_SHORT).show()
            else
            {
                binding.apply {
                    nowTv.text = nowDrink
                    goalTv.text = goalDrink
                    val progress : Int = if(nowDrink.toInt() >= goalDrink.toInt()) 100 else (goalDrink.toInt() / nowDrink.toInt())
                    waveView.setProgress(progress)
                }
                settingSlideUpPopup.dismissAnim()
            }
        }
    }
}