package com.shino72.waterplant.db

import androidx.room.*

@Dao
public interface PlantDao{
    // 전부 가져오기
    @Query("SELECT * FROM 'PlantPicture'")
    suspend fun getAll() : List<PlantPicture>

    @Query("SELECT * FROM 'Calendar' WHERE (Year =:year AND Month =:month) AND Day =:day")
    suspend fun getSingleCal(year : String, month : String, day : String) : Calendar?

    @Query("SELECT * FROM 'PlantPicture' WHERE uid=:uid ")
    suspend fun loadSingleData(uid: String) : PlantPicture?

    @Query("SELECT * FROM 'Calendar'")
    suspend fun getAllCal() : List<Calendar>

    @Update
    suspend fun updateCal(cal:Calendar)
    // 삽입 하기
    @Insert
    suspend fun insertDB(plant: PlantPicture)

    @Insert
    suspend fun insertWaterAll(cal : Calendar)


    // 삭제 하기
    @Delete
    suspend fun delete(plant:PlantPicture)

    @Query("DELETE FROM 'PlantPicture'")
    suspend fun deleteAll()
}