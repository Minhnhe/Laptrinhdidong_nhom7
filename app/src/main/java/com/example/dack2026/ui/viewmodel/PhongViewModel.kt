package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.PhongEntity
import com.example.dack2026.data.repository.PhongRepository
import kotlinx.coroutines.launch

class PhongViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhongRepository
    val allPhong: LiveData<List<PhongEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).phongDao()
        repository = PhongRepository(dao)
        allPhong = repository.allPhong
    }

    fun getPhongByNhaTro(nhaTroId: Long): LiveData<List<PhongEntity>> {
        return repository.getPhongByNhaTro(nhaTroId)
    }

    fun insert(phong: PhongEntity) = viewModelScope.launch {
        repository.insert(phong)
    }

    fun update(phong: PhongEntity) = viewModelScope.launch {
        repository.update(phong)
    }

    fun delete(phong: PhongEntity) = viewModelScope.launch {
        repository.delete(phong)
    }
}
