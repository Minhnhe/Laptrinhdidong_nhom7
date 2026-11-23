package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.KhachThueEntity
import com.example.dack2026.data.repository.KhachThueRepository
import kotlinx.coroutines.launch

class KhachThueViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: KhachThueRepository
    val allKhachThue: LiveData<List<KhachThueEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).khachThueDao()
        repository = KhachThueRepository(dao)
        allKhachThue = repository.allKhachThue
    }

    fun getKhachThueByPhong(phongId: Long): LiveData<List<KhachThueEntity>> {
        return repository.getKhachThueByPhong(phongId)
    }

    fun insert(khachThue: KhachThueEntity) = viewModelScope.launch {
        repository.insert(khachThue)
    }

    fun update(khachThue: KhachThueEntity) = viewModelScope.launch {
        repository.update(khachThue)
    }

    fun delete(khachThue: KhachThueEntity) = viewModelScope.launch {
        repository.delete(khachThue)
    }
}
