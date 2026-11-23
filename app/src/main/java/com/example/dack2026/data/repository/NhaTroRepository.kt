package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.NhaTroDao
import com.example.dack2026.data.entity.NhaTroEntity

class NhaTroRepository(private val nhaTroDao: NhaTroDao) {
    val allNhaTro: LiveData<List<NhaTroEntity>> = nhaTroDao.getAllNhaTro()

    suspend fun insert(nhaTro: NhaTroEntity): Long = nhaTroDao.insert(nhaTro)

    suspend fun update(nhaTro: NhaTroEntity): Int = nhaTroDao.update(nhaTro)

    suspend fun delete(nhaTro: NhaTroEntity): Int = nhaTroDao.delete(nhaTro)

    suspend fun getNhaTroById(id: Long): NhaTroEntity? = nhaTroDao.getNhaTroById(id)
}
