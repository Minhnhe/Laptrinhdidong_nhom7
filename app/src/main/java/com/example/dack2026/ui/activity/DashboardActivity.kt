package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dack2026.R
import com.example.dack2026.data.database.AppDatabase

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvAlertBanner = findViewById<TextView>(R.id.tvAlertBanner)
        val db = AppDatabase.getDatabase(this)
        db.hoaDonDao().getAllHoaDon().observe(this) { hoaDons ->
            val unpaidCount = hoaDons.count { it.trangThai.contains("chưa", true) }
            if (unpaidCount > 0) {
                tvAlertBanner.text = "⚠️ Cảnh báo: Có $unpaidCount hóa đơn chưa thanh toán!"
                tvAlertBanner.setBackgroundColor(0xFFFFF3E0.toInt())
            } else {
                tvAlertBanner.text = "✅ Tất cả hóa đơn đã được thanh toán đầy đủ."
                tvAlertBanner.setBackgroundColor(0xFFE8F5E9.toInt())
            }
        }

        findViewById<Button>(R.id.btnNhaTro).setOnClickListener {
            startActivity(Intent(this, NhaTroActivity::class.java))
        }
        findViewById<Button>(R.id.btnPhong).setOnClickListener {
            startActivity(Intent(this, PhongActivity::class.java))
        }
        findViewById<Button>(R.id.btnKhachThue).setOnClickListener {
            startActivity(Intent(this, KhachThueActivity::class.java))
        }
        findViewById<Button>(R.id.btnHopDong).setOnClickListener {
            startActivity(Intent(this, HopDongActivity::class.java))
        }
        findViewById<Button>(R.id.btnHoaDon).setOnClickListener {
            startActivity(Intent(this, HoaDonActivity::class.java))
        }
        findViewById<Button>(R.id.btnThuChi).setOnClickListener {
            startActivity(Intent(this, ThuChiActivity::class.java))
        }
        findViewById<Button>(R.id.btnThongKe).setOnClickListener {
            startActivity(Intent(this, ThongKeActivity::class.java))
        }
        findViewById<Button>(R.id.btnDarkMode).setOnClickListener {
            val currentMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
            if (currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("⚙️ Cài đặt Hệ thống & Đối tác")
                .setMessage("Phiên bản: 1.0 (Bản bàn giao đối tác)\nCơ sở dữ liệu: SQLite / Room Database\nTrạng thái: Hoạt động ổn định\n\n© 2026 - DACK2026 Rental Management Systems.")
                .setPositiveButton("Đóng", null)
                .setNeutralButton("Đổi mật khẩu") { _, _ ->
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                }
                .show()
        }
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Chặn nút Back hệ thống ở trang chủ để không bị quay lại màn hình Đăng nhập
        // Thay vào đó đưa app về background (thu nhỏ app)
        moveTaskToBack(true)
    }
}
