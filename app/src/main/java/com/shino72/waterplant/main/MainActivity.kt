package com.shino72.waterplant.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.ActivityMainBinding
import com.shino72.waterplant.dialog.SlideUpDialog
import com.shino72.waterplant.global.MyApplication

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var settingContentView : View
    private lateinit var settingSlideUpPopup : SlideUpDialog
    private lateinit var plantContentView : View
    private lateinit var plantSlideUpPopup: SlideUpDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.main = viewModel


        // slideView 동적 추가
        settingContentView = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_setting, null)
        settingSlideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(settingContentView)
            .create()

        plantContentView = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_plant, null)
        plantSlideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(plantContentView)
            .create()


        initSettingSlider()
        initPlantSlider()
        initWaterDialog()
        initNameDialog()

        // binding
        binding.apply {
            settingBtn.setOnClickListener {
                settingContentView.findViewById<EditText>(R.id.now_et).setText(binding.nowTv.text)
                settingContentView.findViewById<EditText>(R.id.goal_et).setText(binding.goalTv.text)
                settingSlideUpPopup.show()
            }
            editPlantBtn.setOnClickListener {
                plantSlideUpPopup.show()
            }
        }

        // observing
        viewModel.apply {
            now.observe(this@MainActivity, Observer {
                binding.nowTv.text = it.toString()
            })
            goal.observe(this@MainActivity, Observer {
                binding.goalTv.text = it.toString()
            })
            name.observe(this@MainActivity, Observer {
                if(it != null) binding.plantNameTv.text = it.toString()
                else binding.plantNameTv.text = "이름을 지어주세요"
            })
            wave.observe(this@MainActivity, Observer {
                binding.waveView.setProgress(it)
                changePlantImage()
            })
        }
    }

    private fun initNameDialog()
    {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_name, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        binding.editNameBtn.setOnClickListener {
            showEditNameDialog(dialogView, alertDialog)
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setContentView(R.layout.dialog_give_water)
    }
    private fun showEditNameDialog(view : View, dialog: AlertDialog)
    {
        dialog.show()
        view.findViewById<AppCompatButton>(R.id.cancel_btn).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<AppCompatButton>(R.id.apply_btn).setOnClickListener {
            val prefs = MyApplication.pref
            val nameValue = view.findViewById<EditText>(R.id.name_et).text.toString()
            if(nameValue != null){
                prefs.setName(nameValue)
                viewModel.valueChange()
            }
            dialog.dismiss()
        }
    }


    private fun initWaterDialog()
    {
        val dialogView = layoutInflater.inflate(R.layout.dialog_give_water, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        binding.giveWaterBtn.setOnClickListener {
            showGiveWaterDialog(dialogView, alertDialog)
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setContentView(R.layout.dialog_give_water)
    }

    private fun changePlantImage()
    {
        val plantProgress = viewModel.wave.value
        var drawable = R.drawable.icon_seed
        if(plantProgress in (0 until 30)) drawable = R.drawable.icon_seed
        else if(plantProgress in (30 until  70)) drawable = R.drawable.icon_plant
        else if(plantProgress in (70 until 100)) drawable = R.drawable.icon_not_rose
        else drawable = R.drawable.icon_rose

        Glide.with(this)
            .load(drawable)
            .into(binding.plantIv)
    }



    private fun showGiveWaterDialog(view : View, dialog: AlertDialog)
    {
        dialog.show()
        view.findViewById<AppCompatButton>(R.id.cancel_btn).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<AppCompatButton>(R.id.apply_btn).setOnClickListener {
            val prefs = MyApplication.pref
            val waterValue = view.findViewById<EditText>(R.id.give_et).text.toString()
            if(waterValue != null && waterValue.toInt() > 0){
                prefs.setNow(prefs.getNow() + waterValue.toInt())
                viewModel.valueChange()
            }
            dialog.dismiss()
        }
    }
    private fun initSettingSlider()
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

                    var progress : Int = if(nowDrink == "0") 0 else (nowDrink.toInt() * 100) / goalDrink.toInt()
                    if(progress > 100) progress = 100
                    waveView.setProgress(progress)
                }

                val prefs = MyApplication.pref
                prefs.setNow(nowDrink.toInt())
                prefs.setGoal(goalDrink.toInt())
                viewModel.valueChange()
                settingSlideUpPopup.dismissAnim()
            }
        }
    }

    private fun initPlantSlider()
    {
        val cancelBtn = plantContentView.findViewById<AppCompatButton>(R.id.cancel_button)
        cancelBtn.setOnClickListener {
            plantSlideUpPopup.dismissAnim()
        }
    }
}