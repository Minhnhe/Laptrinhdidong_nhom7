package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thu_chi")
data class ThuChiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val loai: String, // "Thu", "Chi"
    val soTien: Double,
    val noiDung: String,
    val ngay: String,
    val ghiChu: String
)
