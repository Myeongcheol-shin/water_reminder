package com.shino72.waterplant.paint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shino72.waterplant.databinding.ActivityPaintBinding

class PaintActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPaintBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}