package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.repository.HoaDonRepository
import com.example.dack2026.data.repository.ThuChiRepository
import kotlinx.coroutines.launch

class ThongKeViewModel(application: Application) : AndroidViewModel(application) {
    private val hoaDonRepository: HoaDonRepository
    private val thuChiRepository: ThuChiRepository

    private val _totalRevenue = MutableLiveData<Double>()
    val totalRevenue: LiveData<Double> get() = _totalRevenue

    private val _totalThu = MutableLiveData<Double>()
    val totalThu: LiveData<Double> get() = _totalThu

    private val _totalChi = MutableLiveData<Double>()
    val totalChi: LiveData<Double> get() = _totalChi

    init {
        val db = AppDatabase.getDatabase(application)
        hoaDonRepository = HoaDonRepository(db.hoaDonDao())
        thuChiRepository = ThuChiRepository(db.thuChiDao())
    }

    fun loadStatistics() {
        viewModelScope.launch {
            val rev = hoaDonRepository.getTotalRevenue() ?: 0.0
            val thu = thuChiRepository.getTotalThu() ?: 0.0
            val chi = thuChiRepository.getTotalChi() ?: 0.0
            _totalRevenue.postValue(rev)
            _totalThu.postValue(thu)
            _totalChi.postValue(chi)
        }
    }
}
