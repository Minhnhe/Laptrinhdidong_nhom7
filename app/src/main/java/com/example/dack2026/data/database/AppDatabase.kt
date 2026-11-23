package com.example.dack2026.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dack2026.data.dao.*
import com.example.dack2026.data.entity.*
import com.example.dack2026.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        NhaTroEntity::class,
        PhongEntity::class,
        KhachThueEntity::class,
        HopDongEntity::class,
        HoaDonEntity::class,
        ThuChiEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun nhaTroDao(): NhaTroDao
    abstract fun phongDao(): PhongDao
    abstract fun khachThueDao(): KhachThueDao
    abstract fun hopDongDao(): HopDongDao
    abstract fun hoaDonDao(): HoaDonDao
    abstract fun thuChiDao(): ThuChiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                // Seed default data asynchronously on IO thread once if needed
                CoroutineScope(Dispatchers.IO).launch {
                    seedDefaultAccountsAndProperties(instance)
                }

                instance
            }
        }

        private suspend fun seedDefaultAccountsAndProperties(database: AppDatabase) {
            try {
                val userDao = database.userDao()
                if (userDao.getUserByUsername("admin") == null) {
                    userDao.insertUser(
                        UserEntity(
                            username = "admin",
                            password = "admin123",
                            fullName = "Quản Trị Viên Hệ Thống",
                            phone = "0999999999",
                            role = UserEntity.ROLE_ADMIN
                        )
                    )
                }
                if (userDao.getUserByUsername("chutro") == null) {
                    userDao.insertUser(
                        UserEntity(
                            username = "chutro",
                            password = "123456",
                            fullName = "Chủ Trọ Nguyễn Văn A",
                            phone = "0912345678",
                            role = UserEntity.ROLE_CHU_TRO
                        )
                    )
                }
                if (userDao.getUserByUsername("nguoithue") == null) {
                    userDao.insertUser(
                        UserEntity(
                            username = "nguoithue",
                            password = "123456",
                            fullName = "Khách Thuê Trần Thị B",
                            phone = "0987654321",
                            role = UserEntity.ROLE_NGUOI_THUE
                        )
                    )
                }

                val nhaTroDao = database.nhaTroDao()
                if (nhaTroDao.getNhaTroById(1) == null) {
                    nhaTroDao.insert(NhaTroEntity(tenNhaTro = "Nhà trọ Thanh Nội", diaChi = "Thôn Giả - Xã Nội Hoàng", moTa = "Đầy đủ tiện nghi", giaMacDinh = 1500000.0))
                    nhaTroDao.insert(NhaTroEntity(tenNhaTro = "Nhà trọ An Bình", diaChi = "Đường Nguyễn Văn Cừ, Bắc Ninh", moTa = "Gần KCN Quế Võ", giaMacDinh = 1200000.0))
                    nhaTroDao.insert(NhaTroEntity(tenNhaTro = "Nhà trọ Hồng Phúc", diaChi = "Đường Lý Thái Tổ, Từ Sơn", moTa = "Phòng rộng rãi, giờ giấc tự do", giaMacDinh = 1800000.0))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
