package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.ThuChiDao
import com.example.dack2026.data.entity.ThuChiEntity

class ThuChiRepository(private val thuChiDao: ThuChiDao) {
    val allThuChi: LiveData<List<ThuChiEntity>> = thuChiDao.getAllThuChi()

    suspend fun insert(thuChi: ThuChiEntity): Long = thuChiDao.insert(thuChi)

    suspend fun update(thuChi: ThuChiEntity): Int = thuChiDao.update(thuChi)

    suspend fun delete(thuChi: ThuChiEntity): Int = thuChiDao.delete(thuChi)

    suspend fun getThuChiById(id: Long): ThuChiEntity? = thuChiDao.getThuChiById(id)

    suspend fun getTotalThu(): Double? = thuChiDao.getTotalThu()

    suspend fun getTotalChi(): Double? = thuChiDao.getTotalChi()
}
