package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.HoaDonDao
import com.example.dack2026.data.entity.HoaDonEntity

class HoaDonRepository(private val hoaDonDao: HoaDonDao) {
    val allHoaDon: LiveData<List<HoaDonEntity>> = hoaDonDao.getAllHoaDon()

    suspend fun insert(hoaDon: HoaDonEntity): Long = hoaDonDao.insert(hoaDon)

    suspend fun update(hoaDon: HoaDonEntity): Int = hoaDonDao.update(hoaDon)

    suspend fun delete(hoaDon: HoaDonEntity): Int = hoaDonDao.delete(hoaDon)

    suspend fun getHoaDonById(id: Long): HoaDonEntity? = hoaDonDao.getHoaDonById(id)

    suspend fun getTotalRevenue(): Double? = hoaDonDao.getTotalRevenue()
}
