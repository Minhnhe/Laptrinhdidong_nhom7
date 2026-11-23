package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.PhongEntity

@Dao
interface PhongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(phong: PhongEntity): Long

    @Update
    suspend fun update(phong: PhongEntity): Int

    @Delete
    suspend fun delete(phong: PhongEntity): Int

    @Query("SELECT * FROM phong ORDER BY id DESC")
    fun getAllPhong(): LiveData<List<PhongEntity>>

    @Query("SELECT * FROM phong WHERE nhaTroId = :nhaTroId ORDER BY id DESC")
    fun getPhongByNhaTro(nhaTroId: Long): LiveData<List<PhongEntity>>

    @Query("SELECT * FROM phong WHERE id = :id")
    suspend fun getPhongById(id: Long): PhongEntity?
}
