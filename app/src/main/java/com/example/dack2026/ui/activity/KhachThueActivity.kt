package com.example.dack2026.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dack2026.R
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.KhachThueEntity
import com.example.dack2026.data.entity.PhongEntity
import com.example.dack2026.ui.adapter.KhachThueAdapter
import com.example.dack2026.ui.viewmodel.KhachThueViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KhachThueActivity : AppCompatActivity() {
    private val khachThueViewModel: KhachThueViewModel by viewModels()
    private lateinit var adapter: KhachThueAdapter
    private var phongList: List<PhongEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khach_thue)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Khách thuê"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = KhachThueAdapter(
            onEditClick = { khach -> showAddEditDialog(khach) },
            onDeleteClick = { khach -> confirmDelete(khach) }
        )
        recyclerView.adapter = adapter

        khachThueViewModel.allKhachThue.observe(this) { list ->
            adapter.updateData(list)
        }

        val db = AppDatabase.getDatabase(this)
        db.phongDao().getAllPhong().observe(this) { list ->
            phongList = list
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

    private fun showAddEditDialog(khachThue: KhachThueEntity?) {
        if (phongList.isEmpty()) {
            Toast.makeText(this, "Vui lòng tạo Phòng trước khi thêm Khách Thuê", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_khach_thue, null)
        val spinnerPhong = dialogView.findViewById<Spinner>(R.id.spinnerPhong)
        val etHoTen = dialogView.findViewById<EditText>(R.id.etHoTen)
        val etSdt = dialogView.findViewById<EditText>(R.id.etSdt)
        val etCmnd = dialogView.findViewById<EditText>(R.id.etCmnd)
        val etQueQuan = dialogView.findViewById<EditText>(R.id.etQueQuan)
        val etNgaySinh = dialogView.findViewById<EditText>(R.id.etNgaySinh)
        val etGioiTinh = dialogView.findViewById<EditText>(R.id.etGioiTinh)

        val phongNames = phongList.map { it.tenPhong }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, phongNames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPhong.adapter = spinnerAdapter

        if (khachThue != null) {
            etHoTen.setText(khachThue.hoTen)
            etSdt.setText(khachThue.sdt)
            etCmnd.setText(khachThue.cmnd)
            etQueQuan.setText(khachThue.queQuan)
            etNgaySinh.setText(khachThue.ngaySinh)
            etGioiTinh.setText(khachThue.gioiTinh)
            val index = phongList.indexOfFirst { it.id == khachThue.phongId }
            if (index >= 0) spinnerPhong.setSelection(index)
        } else {
            etGioiTinh.setText("Nam")
        }

        AlertDialog.Builder(this)
            .setTitle(if (khachThue == null) "Thêm Khách Thuê" else "Sửa Khách Thuê")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val selectedIndex = spinnerPhong.selectedItemPosition
                if (selectedIndex < 0 || selectedIndex >= phongList.size) return@setPositiveButton
                val selectedPhong = phongList[selectedIndex]

                val hoTen = etHoTen.text.toString().trim()
                val sdt = etSdt.text.toString().trim()
                val cmnd = etCmnd.text.toString().trim()
                val queQuan = etQueQuan.text.toString().trim()
                val ngaySinh = etNgaySinh.text.toString().trim()
                val gioiTinh = etGioiTinh.text.toString().trim().ifEmpty { "Nam" }

                if (hoTen.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập họ tên và số điện thoại", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (khachThue == null) {
                    val newItem = KhachThueEntity(
                        phongId = selectedPhong.id,
                        hoTen = hoTen,
                        sdt = sdt,
                        cmnd = cmnd,
                        queQuan = queQuan,
                        ngaySinh = ngaySinh,
                        gioiTinh = gioiTinh
                    )
                    khachThueViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm khách thuê", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = khachThue.copy(
                        phongId = selectedPhong.id,
                        hoTen = hoTen,
                        sdt = sdt,
                        cmnd = cmnd,
                        queQuan = queQuan,
                        ngaySinh = ngaySinh,
                        gioiTinh = gioiTinh
                    )
                    khachThueViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật khách thuê", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(khachThue: KhachThueEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Khách Thuê")
            .setMessage("Bạn có chắc chắn muốn xóa ${khachThue.hoTen}?")
            .setPositiveButton("Xóa") { _, _ ->
                khachThueViewModel.delete(khachThue)
                Toast.makeText(this, "Đã xóa khách thuê", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
