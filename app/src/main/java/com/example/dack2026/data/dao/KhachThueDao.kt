package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.KhachThueEntity

@Dao
interface KhachThueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(khachThue: KhachThueEntity): Long

    @Update
    suspend fun update(khachThue: KhachThueEntity): Int

    @Delete
    suspend fun delete(khachThue: KhachThueEntity): Int

    @Query("SELECT * FROM khach_thue ORDER BY id DESC")
    fun getAllKhachThue(): LiveData<List<KhachThueEntity>>

    @Query("SELECT * FROM khach_thue WHERE phongId = :phongId ORDER BY id DESC")
    fun getKhachThueByPhong(phongId: Long): LiveData<List<KhachThueEntity>>

    @Query("SELECT * FROM khach_thue WHERE id = :id")
    suspend fun getKhachThueById(id: Long): KhachThueEntity?
}
