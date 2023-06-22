package com.shino72.waterplant.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface PlantDao{
    // 전부 가져오기
    @Query("SELECT * FROM 'PlantPicture'")
    suspend fun getAll() : List<PlantPicture>

    // 삽입 하기
    @Insert
    suspend fun insertDB(plant: PlantPicture)

    // 삭제 하기
    @Delete
    suspend fun delete(plant:PlantPicture)
}