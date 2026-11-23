package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nha_tro")
data class NhaTroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tenNhaTro: String,
    val diaChi: String,
    val moTa: String,
    val giaMacDinh: Double
)
