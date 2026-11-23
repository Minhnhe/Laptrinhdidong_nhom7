package com.example.dack2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    val fullName: String,
    val phone: String,
    val role: String = ROLE_CHU_TRO
) {
    companion object {
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_CHU_TRO = "CHU_TRO"
        const val ROLE_NGUOI_THUE = "NGUOI_THUE"
    }
}
