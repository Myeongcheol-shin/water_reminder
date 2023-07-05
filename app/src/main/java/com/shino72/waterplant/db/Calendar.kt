package com.shino72.waterplant.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calendar(
    val Year : Int,
    val Month : Int,
    val Day : Int,
    var Size : Int,
    @PrimaryKey(autoGenerate = true) var uid : Int = 0
)