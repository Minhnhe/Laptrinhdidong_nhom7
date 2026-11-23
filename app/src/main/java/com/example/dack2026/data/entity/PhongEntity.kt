package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phong")
data class PhongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nhaTroId: Long,
    val tenPhong: String,
    val dienTich: Double,
    val giaThue: Double,
    val trangThai: String, // "Trống", "Đang thuê"
    val moTa: String
)
