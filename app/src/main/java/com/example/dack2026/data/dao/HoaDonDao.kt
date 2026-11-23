package com.example.dack2026.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dack2026.data.entity.HoaDonEntity

@Dao
interface HoaDonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hoaDon: HoaDonEntity): Long

    @Update
    suspend fun update(hoaDon: HoaDonEntity): Int

    @Delete
    suspend fun delete(hoaDon: HoaDonEntity): Int

    @Query("SELECT * FROM hoa_don ORDER BY id DESC")
    fun getAllHoaDon(): LiveData<List<HoaDonEntity>>

    @Query("SELECT * FROM hoa_don WHERE id = :id")
    suspend fun getHoaDonById(id: Long): HoaDonEntity?

    @Query("SELECT SUM(tongTien) FROM hoa_don WHERE trangThai = 'Đã thanh toán'")
    suspend fun getTotalRevenue(): Double?
}
