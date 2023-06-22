package com.shino72.waterplant.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlantPicture(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val name : String,
    val image1 : Bitmap,
    val image2 : Bitmap,
    val image3 : Bitmap,
    val image4 : Bitmap,

)