package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dack2026.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
