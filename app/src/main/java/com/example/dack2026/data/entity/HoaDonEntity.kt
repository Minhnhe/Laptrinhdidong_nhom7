package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hoa_don")
data class HoaDonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phongId: Long,
    val thangNam: String,
    val chiSoDienCu: Int,
    val chiSoDienMoi: Int,
    val donGiaDien: Double,
    val chiSoNuocCu: Int,
    val chiSoNuocMoi: Int,
    val donGiaNuoc: Double,
    val tienPhong: Double,
    val tienWifi: Double = 50000.0,
    val tienRac: Double = 30000.0,
    val tienDichVu: Double = 0.0,
    val tongTien: Double,
    val trangThai: String, // "Chưa thanh toán", "Đã thanh toán"
    val ngayTao: String
)
