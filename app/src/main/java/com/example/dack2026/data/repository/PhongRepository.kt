package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.PhongDao
import com.example.dack2026.data.entity.PhongEntity

class PhongRepository(private val phongDao: PhongDao) {
    val allPhong: LiveData<List<PhongEntity>> = phongDao.getAllPhong()

    fun getPhongByNhaTro(nhaTroId: Long): LiveData<List<PhongEntity>> = phongDao.getPhongByNhaTro(nhaTroId)

    suspend fun insert(phong: PhongEntity): Long = phongDao.insert(phong)

    suspend fun update(phong: PhongEntity): Int = phongDao.update(phong)

    suspend fun delete(phong: PhongEntity): Int = phongDao.delete(phong)

    suspend fun getPhongById(id: Long): PhongEntity? = phongDao.getPhongById(id)
}
