package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "khach_thue")
data class KhachThueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phongId: Long,
    val hoTen: String,
    val sdt: String,
    val cmnd: String,
    val queQuan: String,
    val ngaySinh: String,
    val gioiTinh: String
)
