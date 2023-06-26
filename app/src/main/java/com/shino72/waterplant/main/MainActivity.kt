package com.shino72.waterplant.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shino72.waterplant.Adapter.Plant
import com.shino72.waterplant.Adapter.PlantListAdapter
import com.shino72.waterplant.R
import com.shino72.waterplant.db.AppDataBase
import com.shino72.waterplant.db.PlantPicture
import com.shino72.waterplant.dialog.SlideUpDialog
import com.shino72.waterplant.global.MyApplication
import com.shino72.waterplant.ui.calender.CalenderActivity
import com.shino72.waterplant.paint.PaintActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val binding by lazy { com.shino72.waterplant.databinding.ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var settingContentView: View
    private lateinit var settingSlideUpPopup: SlideUpDialog
    private lateinit var plantContentView: View
    private lateinit var plantSlideUpPopup: SlideUpDialog
    private lateinit var plantList: MutableList<Plant>
    private var AppDataBase : AppDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.main = viewModel

        // slideView 동적 추가
        settingContentView =
            (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.layout_setting,
                null
            )
        settingSlideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(settingContentView)
            .create()

        plantContentView =
            (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.layout_plant,
                null
            )
        plantSlideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(plantContentView)
            .create()

        initRoomDB()

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

            calBtn.setOnClickListener {
                val calIntent = Intent(this@MainActivity, CalenderActivity::class.java)
                startActivity(calIntent)
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
                if (it != null) binding.plantNameTv.text = it.toString()
                else binding.plantNameTv.text = "이름을 지어주세요"
            })
            wave.observe(this@MainActivity, Observer {
                reLoadImage(it)
            })
            progress.observe(this@MainActivity, Observer {
                binding.waveView.setProgress(it)
            })
        }
    }

    private fun initRoomDB()
    {
        AppDataBase = com.shino72.waterplant.db.AppDataBase.getInstance(this)
    }

    private fun initNameDialog() {
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

    private fun showEditNameDialog(view: View, dialog: AlertDialog) {
        dialog.show()
        view.findViewById<AppCompatButton>(R.id.cancel_btn).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<AppCompatButton>(R.id.apply_btn).setOnClickListener {
            val prefs = MyApplication.pref
            val nameValue = view.findViewById<EditText>(R.id.name_et).text.toString()
            if (nameValue != null) {
                prefs.setName(nameValue)
                viewModel.valueChange()
            }
            dialog.dismiss()
        }
    }


    private fun initWaterDialog() {
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


    private fun showGiveWaterDialog(view: View, dialog: AlertDialog) {
        dialog.show()
        view.findViewById<AppCompatButton>(R.id.cancel_btn).setOnClickListener {
            dialog.dismiss()
            //Toast.makeText(applicationContext, "${view.findViewById<TextView>(R.id.tv).text}", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<AppCompatButton>(R.id.apply_btn).setOnClickListener {
            val prefs = MyApplication.pref
            val waterValue = view.findViewById<EditText>(R.id.give_et).text.toString()
            if (waterValue != null && waterValue.toInt() > 0) {
                prefs.setNow(prefs.getNow() + waterValue.toInt())
                viewModel.valueChange()
            }
            dialog.dismiss()
        }
    }

    private fun initSettingSlider() {
        val cancelBtn = settingContentView.findViewById<AppCompatButton>(R.id.cancel_button)
        val applyBtn = settingContentView.findViewById<AppCompatButton>(R.id.apply_btn)

        settingContentView.findViewById<EditText>(R.id.goal_et).setText(MyApplication.pref.getGoal().toString())
        settingContentView.findViewById<EditText>(R.id.now_et).setText(MyApplication.pref.getNow().toString())

        cancelBtn.setOnClickListener {
            settingSlideUpPopup.dismissAnim()
        }

        applyBtn.setOnClickListener {

            val nowDrink = settingContentView.findViewById<EditText>(R.id.now_et).text.toString()
            val goalDrink = settingContentView.findViewById<EditText>(R.id.goal_et).text.toString()

            if (goalDrink == "0") Toast.makeText(this, "목표는 0이 될 수 없습니다", Toast.LENGTH_SHORT).show()
            else {
                binding.apply {

                    var progress: Int =
                        if (nowDrink == "0") 0 else (nowDrink.toInt() * 100) / goalDrink.toInt()
                    if (progress > 100) progress = 100
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
    private fun reLoadImage(progress : Int)
    {
        runBlocking {
            var singleData : PlantPicture? = null
            var drawable : Bitmap? = null
            withContext(coroutineContext) {
                singleData = AppDataBase!!.PlantDao().loadSingleData(MyApplication.pref.getSelectedId().toString())
            }
            withContext(coroutineContext){
                when(progress)
                {
                    1 -> {
                        drawable = singleData!!.image1
                    }
                    2 -> {
                        drawable = singleData!!.image2
                    }
                    3 -> {
                        drawable = singleData!!.image3
                    }
                    4 -> {
                        drawable = singleData!!.image4
                    }
                }
                binding.plantNameTv.text = singleData!!.name
                Glide.with(applicationContext)
                    .load(drawable)
                    .into(binding.plantIv)
            }
        }
    }

    private fun initPlantSlider() {
        val cancelBtn = plantContentView.findViewById<AppCompatButton>(R.id.cancel_button)
        cancelBtn.setOnClickListener {
            plantSlideUpPopup.dismissAnim()
        }

        val plantListAdapter = PlantListAdapter()
        val layoutManager = GridLayoutManager(this, 1)
        plantList = mutableListOf()
        with(plantContentView) {
            findViewById<RecyclerView>(R.id.rc).apply {
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                adapter = plantListAdapter
            }
        }
        runBlocking {
            withContext(this.coroutineContext) {

                val dao = AppDataBase!!.PlantDao()
                dao.getAll().forEach {
                    plantList.add(
                        Plant(
                            it.name,
                            it.image1,
                            it.image2,
                            it.image3,
                            it.image4,
                            it.uid
                        )
                    )
                }
            }
            withContext(this.coroutineContext)
            {
                plantListAdapter.setItem(plantList)
            }
        }
        plantListAdapter.setItemClickListener(object : PlantListAdapter.ItemClickListener {
            override fun onClick(view: View, cbItem: Plant, position: Int) {
                when (view.id) {
                    R.id.item_cb -> {
                        Toast.makeText(applicationContext, "${cbItem.name}", Toast.LENGTH_SHORT)
                            .show()

                        MyApplication.pref.setSelectedId(cbItem.id)
                        reLoadImage(progress = viewModel.wave.value!!)
                    }

                    R.id.delete_btn -> {
                        plantListAdapter.notifyItemRangeRemoved(0, plantList.size);
                        Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_SHORT)
                            .show()
                        plantListAdapter.notifyItemRangeChanged(0, plantList.size - 1)
                        plantList.remove(cbItem)
                    }
                }
            }
        })

        val addBtn = plantContentView.findViewById<AppCompatButton>(R.id.add_btn)
        addBtn.setOnClickListener {
            val intent = Intent(this, PaintActivity::class.java)
            startActivity(intent)
        }
    }
}