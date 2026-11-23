package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.NhaTroEntity

@Dao
interface NhaTroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nhaTro: NhaTroEntity): Long

    @Update
    suspend fun update(nhaTro: NhaTroEntity): Int

    @Delete
    suspend fun delete(nhaTro: NhaTroEntity): Int

    @Query("SELECT * FROM nha_tro ORDER BY id DESC")
    fun getAllNhaTro(): LiveData<List<NhaTroEntity>>

    @Query("SELECT * FROM nha_tro WHERE id = :id")
    suspend fun getNhaTroById(id: Long): NhaTroEntity?
}
