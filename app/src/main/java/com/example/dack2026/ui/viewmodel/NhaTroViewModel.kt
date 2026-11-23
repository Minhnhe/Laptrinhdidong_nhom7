package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.NhaTroEntity
import com.example.dack2026.data.repository.NhaTroRepository
import kotlinx.coroutines.launch

class NhaTroViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NhaTroRepository
    val allNhaTro: LiveData<List<NhaTroEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).nhaTroDao()
        repository = NhaTroRepository(dao)
        allNhaTro = repository.allNhaTro
    }

    fun insert(nhaTro: NhaTroEntity) = viewModelScope.launch {
        repository.insert(nhaTro)
    }

    fun update(nhaTro: NhaTroEntity) = viewModelScope.launch {
        repository.update(nhaTro)
    }

    fun delete(nhaTro: NhaTroEntity) = viewModelScope.launch {
        repository.delete(nhaTro)
    }
}
