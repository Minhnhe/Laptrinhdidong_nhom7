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
    version = 3,
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

                val khachDao = database.khachThueDao()
                if (khachDao.getAllList().isEmpty()) {
                    val nhaTroDao = database.nhaTroDao()
                    val ntId = nhaTroDao.insert(
                        NhaTroEntity(
                            tenNhaTro = "Nhà trọ Thanh Nội (Chủ trọ Nguyễn Văn A)",
                            diaChi = "Thôn Giả - Xã Nội Hoàng, Yên Dũng, Bắc Giang",
                            moTa = "Dãy trọ cao cấp 10 phòng, an ninh tuyệt đối",
                            giaMacDinh = 1500000.0
                        )
                    )

                    val phongDao = database.phongDao()
                    val hoaDonDao = database.hoaDonDao()
                    val thuChiDao = database.thuChiDao()

                    val tenantNames = listOf(
                        "Nguyễn Văn Hùng", "Trần Thị Mai", "Lê Văn Nam", "Phạm Thị Lan",
                        "Hoàng Văn Tuấn", "Vũ Thị Hoa", "Bùi Văn Đức", "Đỗ Thị Thu",
                        "Ngô Văn Hải", "Dương Thị Hồng"
                    )
                    val tenantPhones = listOf(
                        "0971111222", "0982222333", "0913333444", "0934444555",
                        "0955555666", "0966666777", "0977777888", "0988888999",
                        "0901112233", "0904445566"
                    )
                    val tenantCccd = listOf(
                        "012345678901", "012345678902", "012345678903", "012345678904",
                        "012345678905", "012345678906", "012345678907", "012345678908",
                        "012345678909", "012345678910"
                    )

                    for (i in 1..10) {
                        val roomName = "Phòng 1$i"
                        val price = 1500000.0 + (i * 100000.0) // 1.5tr to 2.4tr
                        val pId = phongDao.insert(
                            PhongEntity(
                                nhaTroId = ntId,
                                tenPhong = roomName,
                                dienTich = 22.0,
                                giaThue = price,
                                trangThai = "Đã thuê",
                                moTa = "Phòng tiêu chuẩn đầy đủ tiện nghi"
                            )
                        )

                        khachDao.insert(
                            KhachThueEntity(
                                phongId = pId,
                                hoTen = tenantNames[i - 1],
                                sdt = tenantPhones[i - 1],
                                cmnd = tenantCccd[i - 1],
                                queQuan = "Tỉnh Bắc Ninh / Bắc Giang",
                                ngaySinh = "15/0${i}/1998",
                                gioiTinh = if (i % 2 == 0) "Nữ" else "Nam"
                            )
                        )

                        // Invoices for past 5 months (Jan - May 2026) -> Paid
                        val months = listOf("01/2026", "02/2026", "03/2026", "04/2026", "05/2026")
                        for ((mIndex, m) in months.withIndex()) {
                            val dienM = 90 + (mIndex * 30) + (i * 5)
                            val dienC = 90 + ((mIndex - 1).coerceAtLeast(0) * 30) + (i * 5)
                            val nuocM = 12 + (mIndex * 5) + (i * 2)
                            val nuocC = 12 + ((mIndex - 1).coerceAtLeast(0) * 5) + (i * 2)
                            val tDien = (dienM - dienC) * 3500.0
                            val tNuoc = (nuocM - nuocC) * 10000.0
                            val tWifi = 50000.0
                            val tRac = 30000.0
                            val tong = tDien + tNuoc + price + tWifi + tRac

                            hoaDonDao.insert(
                                HoaDonEntity(
                                    phongId = pId,
                                    thangNam = m,
                                    chiSoDienCu = dienC,
                                    chiSoDienMoi = dienM,
                                    donGiaDien = 3500.0,
                                    chiSoNuocCu = nuocC,
                                    chiSoNuocMoi = nuocM,
                                    donGiaNuoc = 10000.0,
                                    tienPhong = price,
                                    tienWifi = tWifi,
                                    tienRac = tRac,
                                    tongTien = tong,
                                    trangThai = "Đã thanh toán",
                                    ngayTao = "01/$m"
                                )
                            )
                        }

                        // Current month invoice (06/2026) -> Unpaid (since month is ongoing)
                        hoaDonDao.insert(
                            HoaDonEntity(
                                phongId = pId,
                                thangNam = "06/2026",
                                chiSoDienCu = 240,
                                chiSoDienMoi = 290,
                                donGiaDien = 3500.0,
                                chiSoNuocCu = 35,
                                chiSoNuocMoi = 43,
                                donGiaNuoc = 10000.0,
                                tienPhong = price,
                                tienWifi = 50000.0,
                                tienRac = 30000.0,
                                tongTien = price + (50 * 3500.0) + (8 * 10000.0) + 50000.0 + 30000.0,
                                trangThai = "Chưa thanh toán",
                                ngayTao = "01/06/2026"
                            )
                        )
                    }

                    // Seed general income / expenses
                    thuChiDao.insert(ThuChiEntity(loai = "Thu", soTien = 17500000.0, noiDung = "Tổng thu tiền phòng 10 phòng tháng 01/2026", ngay = "05/01/2026", ghiChu = "Đã thu đủ"))
                    thuChiDao.insert(ThuChiEntity(loai = "Thu", soTien = 17500000.0, noiDung = "Tổng thu tiền phòng 10 phòng tháng 02/2026", ngay = "05/02/2026", ghiChu = "Đã thu đủ"))
                    thuChiDao.insert(ThuChiEntity(loai = "Chi", soTien = 1200000.0, noiDung = "Bảo trì bơm nước tổng dãy trọ", ngay = "14/03/2026", ghiChu = "Thay tụ bơm nước"))
                    thuChiDao.insert(ThuChiEntity(loai = "Thu", soTien = 17500000.0, noiDung = "Tổng thu tiền phòng 10 phòng tháng 04/2026", ngay = "05/04/2026", ghiChu = "Đã thu đủ"))
                    thuChiDao.insert(ThuChiEntity(loai = "Chi", soTien = 800000.0, noiDung = "Sửa hệ thống đèn chiếu sáng hành lang", ngay = "18/05/2026", ghiChu = "Mua bóng đèn LED"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
