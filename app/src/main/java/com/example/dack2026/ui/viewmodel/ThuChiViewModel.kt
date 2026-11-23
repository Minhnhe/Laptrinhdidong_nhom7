package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.ThuChiEntity
import com.example.dack2026.data.repository.ThuChiRepository
import kotlinx.coroutines.launch

class ThuChiViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ThuChiRepository
    val allThuChi: LiveData<List<ThuChiEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).thuChiDao()
        repository = ThuChiRepository(dao)
        allThuChi = repository.allThuChi
    }

    fun insert(thuChi: ThuChiEntity) = viewModelScope.launch {
        repository.insert(thuChi)
    }

    fun update(thuChi: ThuChiEntity) = viewModelScope.launch {
        repository.update(thuChi)
    }

    fun delete(thuChi: ThuChiEntity) = viewModelScope.launch {
        repository.delete(thuChi)
    }
}
