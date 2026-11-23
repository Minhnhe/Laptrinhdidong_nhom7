package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hop_dong")
data class HopDongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phongId: Long,
    val khachThueId: Long,
    val ngayBatDau: String,
    val ngayKetThuc: String,
    val tienCoc: Double,
    val giaThue: Double,
    val trangThai: String // "Hiệu lực", "Đã thanh lý"
)
