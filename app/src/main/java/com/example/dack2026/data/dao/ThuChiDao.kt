package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.ThuChiEntity

@Dao
interface ThuChiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thuChi: ThuChiEntity): Long

    @Update
    suspend fun update(thuChi: ThuChiEntity): Int

    @Delete
    suspend fun delete(thuChi: ThuChiEntity): Int

    @Query("SELECT * FROM thu_chi ORDER BY id DESC")
    fun getAllThuChi(): LiveData<List<ThuChiEntity>>

    @Query("SELECT * FROM thu_chi WHERE id = :id")
    suspend fun getThuChiById(id: Long): ThuChiEntity?

    @Query("SELECT SUM(soTien) FROM thu_chi WHERE loai = 'Thu'")
    suspend fun getTotalThu(): Double?

    @Query("SELECT SUM(soTien) FROM thu_chi WHERE loai = 'Chi'")
    suspend fun getTotalChi(): Double?
}
