package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.HoaDonEntity
import com.example.dack2026.data.repository.HoaDonRepository
import kotlinx.coroutines.launch

class HoaDonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HoaDonRepository
    val allHoaDon: LiveData<List<HoaDonEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).hoaDonDao()
        repository = HoaDonRepository(dao)
        allHoaDon = repository.allHoaDon
    }

    fun insert(hoaDon: HoaDonEntity) = viewModelScope.launch {
        repository.insert(hoaDon)
    }

    fun update(hoaDon: HoaDonEntity) = viewModelScope.launch {
        repository.update(hoaDon)
    }

    fun delete(hoaDon: HoaDonEntity) = viewModelScope.launch {
        repository.delete(hoaDon)
    }
}
