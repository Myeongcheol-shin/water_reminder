package com.shino72.waterplant.ui.calender

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.ActivityCalenderBinding
import java.time.Year


class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private var selectedBtn = 0
    private lateinit var itemButtonList: List<CalView>

    private val monthFragment = MonthFragment()
    private val weekFragment = WeekFragment()
    private val yearFragment = YearFragment()

    private data class CalView(val cv : CardView, val ll : LinearLayout, val ib : ImageButton, val tv: TextView)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemButtonList = listOf(
            CalView(binding.weekCv, binding.weekLl, binding.weekBtn, binding.weekTv),
            CalView(binding.monthCv,binding.monthLl, binding.monthBtn, binding.monthTv),
            CalView(binding.yearCv, binding.yearLl, binding.yearBtn, binding.yearTv)
        )
        supportFragmentManager.beginTransaction().add(R.id.fl, weekFragment).commit()

        binding.apply {
            itemButtonList.forEachIndexed { index, cv ->
                cv.ib.setOnClickListener {
                    setBtnClick(index)
                    changeFragment(index)
                }
            }
        }
    }

    fun changeFragment(index : Int)
    {
        when(index)
        {
            0 -> {
                hideFragment()
                supportFragmentManager.beginTransaction().show(weekFragment).commit()
            }
            1 -> {
                if(!(monthFragment in supportFragmentManager.fragments)) {
                    supportFragmentManager.beginTransaction().add(R.id.fl, monthFragment).commit()
                }
                hideFragment()
                supportFragmentManager.beginTransaction().show(monthFragment).commit()
            }
            2 -> {
                if(!(yearFragment in supportFragmentManager.fragments)) {
                    supportFragmentManager.beginTransaction().add(R.id.fl, yearFragment).commit()
                }
                hideFragment()
                supportFragmentManager.beginTransaction().show(yearFragment).commit()
            }
        }
    }

    fun hideFragment()
    {
        supportFragmentManager.beginTransaction().hide(weekFragment).commit()
        supportFragmentManager.beginTransaction().hide(monthFragment).commit()
        supportFragmentManager.beginTransaction().hide(yearFragment).commit()
    }
    fun setBtnClick(item : Int)
    {
        itemButtonList[selectedBtn].tv.visibility = View.GONE
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(
            com.shino72.waterplant.R.color.white))

        val param1 = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        val mDp  : Int = (binding.root.resources.displayMetrics.density * 10).toInt()
        param1.setMargins(
            mDp,mDp,mDp,mDp
        )
        itemButtonList[selectedBtn].ll.setBackgroundColor(android.graphics.Color.BLACK)


        itemButtonList[selectedBtn].cv.layoutParams = param1

        val param2 = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        param2.setMargins(
            mDp,mDp,mDp,mDp
        )

        selectedBtn = item
        itemButtonList[selectedBtn].tv.visibility = View.VISIBLE
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(
            com.shino72.waterplant.R.color.black))

        val showAnimation = AlphaAnimation(0f, 1f)
        showAnimation.duration = 1000

        itemButtonList[selectedBtn].ll.background = ContextCompat.getDrawable(this, com.shino72.waterplant.R.drawable.gradation_green)


        itemButtonList[selectedBtn].tv.animation = showAnimation
        param2.weight = 1f
        itemButtonList[selectedBtn].cv.layoutParams = param2
    }
}