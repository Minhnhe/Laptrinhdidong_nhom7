package com.example.dack2026.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dack2026.R
import com.example.dack2026.data.entity.ThuChiEntity
import com.example.dack2026.ui.adapter.ThuChiAdapter
import com.example.dack2026.ui.viewmodel.ThuChiViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class ThuChiActivity : AppCompatActivity() {
    private val thuChiViewModel: ThuChiViewModel by viewModels()
    private lateinit var adapter: ThuChiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thu_chi)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { finish() }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Thu - Chi"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ThuChiAdapter(
            onEditClick = { thuChi -> showAddEditDialog(thuChi) },
            onDeleteClick = { thuChi -> confirmDelete(thuChi) }
        )
        recyclerView.adapter = adapter

        thuChiViewModel.allThuChi.observe(this) { list ->
            adapter.updateData(list)
        }

        fabAdd.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddEditDialog(thuChi: ThuChiEntity?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_thu_chi, null)
        val etLoai = dialogView.findViewById<EditText>(R.id.etLoai)
        val etSoTien = dialogView.findViewById<EditText>(R.id.etSoTien)
        val etNoiDung = dialogView.findViewById<EditText>(R.id.etNoiDung)
        val etNgay = dialogView.findViewById<EditText>(R.id.etNgay)
        val etGhiChu = dialogView.findViewById<EditText>(R.id.etGhiChu)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        if (thuChi != null) {
            etLoai.setText(thuChi.loai)
            etSoTien.setText(thuChi.soTien.toString())
            etNoiDung.setText(thuChi.noiDung)
            etNgay.setText(thuChi.ngay)
            etGhiChu.setText(thuChi.ghiChu)
        } else {
            etLoai.setText("Thu")
            etNgay.setText(currentDate)
        }

        AlertDialog.Builder(this)
            .setTitle(if (thuChi == null) "Thêm Thu - Chi" else "Sửa Thu - Chi")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val loai = etLoai.text.toString().trim().ifEmpty { "Thu" }
                val soTienStr = etSoTien.text.toString().trim()
                val noiDung = etNoiDung.text.toString().trim()
                val ngay = etNgay.text.toString().trim().ifEmpty { currentDate }
                val ghiChu = etGhiChu.text.toString().trim()

                if (noiDung.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val soTien = soTienStr.toDoubleOrNull() ?: 0.0

                if (thuChi == null) {
                    val newItem = ThuChiEntity(loai = loai, soTien = soTien, noiDung = noiDung, ngay = ngay, ghiChu = ghiChu)
                    thuChiViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm giao dịch", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = thuChi.copy(loai = loai, soTien = soTien, noiDung = noiDung, ngay = ngay, ghiChu = ghiChu)
                    thuChiViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật giao dịch", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(thuChi: ThuChiEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Giao Dịch")
            .setMessage("Bạn có chắc chắn muốn xóa giao dịch này?")
            .setPositiveButton("Xóa") { _, _ ->
                thuChiViewModel.delete(thuChi)
                Toast.makeText(this, "Đã xóa giao dịch", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
