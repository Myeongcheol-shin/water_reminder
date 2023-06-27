package com.shino72.waterplant.ui.calender

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.shino72.waterplant.R
import com.shino72.waterplant.databinding.ActivityCalenderBinding


class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private var selectedBtn = 0
    private lateinit var itemButtonList: List<CalView>

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

        binding.apply {
            itemButtonList.forEachIndexed { index, cv ->
                cv.ib.setOnClickListener {
                    setBtnClick(index)
                }
            }
        }
    }

    fun setBtnClick(item : Int)
    {
        itemButtonList[selectedBtn].tv.visibility = View.GONE
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))

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
        itemButtonList[selectedBtn].ib.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))

        val showAnimation = AlphaAnimation(0f, 1f)
        showAnimation.duration = 1000

        itemButtonList[selectedBtn].ll.background = ContextCompat.getDrawable(this, R.drawable.gradation_green)


        itemButtonList[selectedBtn].tv.animation = showAnimation
        param2.weight = 1f
        itemButtonList[selectedBtn].cv.layoutParams = param2
    }
}