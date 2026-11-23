package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.HopDongEntity

@Dao
interface HopDongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hopDong: HopDongEntity): Long

    @Update
    suspend fun update(hopDong: HopDongEntity): Int

    @Delete
    suspend fun delete(hopDong: HopDongEntity): Int

    @Query("SELECT * FROM hop_dong ORDER BY id DESC")
    fun getAllHopDong(): LiveData<List<HopDongEntity>>

    @Query("SELECT * FROM hop_dong WHERE id = :id")
    suspend fun getHopDongById(id: Long): HopDongEntity?
}
