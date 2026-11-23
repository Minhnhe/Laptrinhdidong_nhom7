package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.HopDongDao
import com.example.dack2026.data.entity.HopDongEntity

class HopDongRepository(private val hopDongDao: HopDongDao) {
    val allHopDong: LiveData<List<HopDongEntity>> = hopDongDao.getAllHopDong()

    suspend fun insert(hopDong: HopDongEntity): Long = hopDongDao.insert(hopDong)

    suspend fun update(hopDong: HopDongEntity): Int = hopDongDao.update(hopDong)

    suspend fun delete(hopDong: HopDongEntity): Int = hopDongDao.delete(hopDong)

    suspend fun getHopDongById(id: Long): HopDongEntity? = hopDongDao.getHopDongById(id)
}
