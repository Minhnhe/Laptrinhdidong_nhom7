package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.HopDongEntity
import com.example.dack2026.data.repository.HopDongRepository
import kotlinx.coroutines.launch

class HopDongViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HopDongRepository
    val allHopDong: LiveData<List<HopDongEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).hopDongDao()
        repository = HopDongRepository(dao)
        allHopDong = repository.allHopDong
    }

    fun insert(hopDong: HopDongEntity) = viewModelScope.launch {
        repository.insert(hopDong)
    }

    fun update(hopDong: HopDongEntity) = viewModelScope.launch {
        repository.update(hopDong)
    }

    fun delete(hopDong: HopDongEntity) = viewModelScope.launch {
        repository.delete(hopDong)
    }
}
