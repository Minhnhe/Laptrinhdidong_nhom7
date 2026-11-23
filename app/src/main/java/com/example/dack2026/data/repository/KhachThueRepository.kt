package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.KhachThueDao
import com.example.dack2026.data.entity.KhachThueEntity

class KhachThueRepository(private val khachThueDao: KhachThueDao) {
    val allKhachThue: LiveData<List<KhachThueEntity>> = khachThueDao.getAllKhachThue()

    fun getKhachThueByPhong(phongId: Long): LiveData<List<KhachThueEntity>> = khachThueDao.getKhachThueByPhong(phongId)

    suspend fun insert(khachThue: KhachThueEntity): Long = khachThueDao.insert(khachThue)

    suspend fun update(khachThue: KhachThueEntity): Int = khachThueDao.update(khachThue)

    suspend fun delete(khachThue: KhachThueEntity): Int = khachThueDao.delete(khachThue)

    suspend fun getKhachThueById(id: Long): KhachThueEntity? = khachThueDao.getKhachThueById(id)
}
