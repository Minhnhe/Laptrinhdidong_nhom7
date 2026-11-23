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
import com.example.dack2026.data.entity.HopDongEntity
import com.example.dack2026.data.entity.KhachThueEntity
import com.example.dack2026.data.entity.PhongEntity
import com.example.dack2026.ui.adapter.HopDongAdapter
import com.example.dack2026.ui.viewmodel.HopDongViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HopDongActivity : AppCompatActivity() {
    private val hopDongViewModel: HopDongViewModel by viewModels()
    private lateinit var adapter: HopDongAdapter
    private var phongList: List<PhongEntity> = emptyList()
    private var khachThueList: List<KhachThueEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hop_dong)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Hợp đồng"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HopDongAdapter(
            onEditClick = { hopDong -> showAddEditDialog(hopDong) },
            onDeleteClick = { hopDong -> confirmDelete(hopDong) }
        )
        recyclerView.adapter = adapter

        hopDongViewModel.allHopDong.observe(this) { list ->
            adapter.updateData(list)
        }

        val db = AppDatabase.getDatabase(this)
        db.phongDao().getAllPhong().observe(this) { list ->
            phongList = list
        }
        db.khachThueDao().getAllKhachThue().observe(this) { list ->
            khachThueList = list
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

    private fun showAddEditDialog(hopDong: HopDongEntity?) {
        if (phongList.isEmpty() || khachThueList.isEmpty()) {
            Toast.makeText(this, "Cần có ít nhất 1 Phòng và 1 Khách Thuê", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_hop_dong, null)
        val spinnerPhong = dialogView.findViewById<Spinner>(R.id.spinnerPhong)
        val spinnerKhachThue = dialogView.findViewById<Spinner>(R.id.spinnerKhachThue)
        val etNgayBatDau = dialogView.findViewById<EditText>(R.id.etNgayBatDau)
        val etNgayKetThuc = dialogView.findViewById<EditText>(R.id.etNgayKetThuc)
        val etTienCoc = dialogView.findViewById<EditText>(R.id.etTienCoc)
        val etGiaThue = dialogView.findViewById<EditText>(R.id.etGiaThue)
        val etTrangThai = dialogView.findViewById<EditText>(R.id.etTrangThai)

        val phongNames = phongList.map { it.tenPhong }
        val phongAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, phongNames)
        phongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPhong.adapter = phongAdapter

        val khachNames = khachThueList.map { it.hoTen }
        val khachAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, khachNames)
        khachAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKhachThue.adapter = khachAdapter

        if (hopDong != null) {
            etNgayBatDau.setText(hopDong.ngayBatDau)
            etNgayKetThuc.setText(hopDong.ngayKetThuc)
            etTienCoc.setText(hopDong.tienCoc.toString())
            etGiaThue.setText(hopDong.giaThue.toString())
            etTrangThai.setText(hopDong.trangThai)

            val pIndex = phongList.indexOfFirst { it.id == hopDong.phongId }
            if (pIndex >= 0) spinnerPhong.setSelection(pIndex)

            val kIndex = khachThueList.indexOfFirst { it.id == hopDong.khachThueId }
            if (kIndex >= 0) spinnerKhachThue.setSelection(kIndex)
        } else {
            etTrangThai.setText("Hiệu lực")
            if (phongList.isNotEmpty()) etGiaThue.setText(phongList[0].giaThue.toString())
        }

        AlertDialog.Builder(this)
            .setTitle(if (hopDong == null) "Thêm Hợp Đồng" else "Sửa Hợp Đồng")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val pPos = spinnerPhong.selectedItemPosition
                val kPos = spinnerKhachThue.selectedItemPosition
                if (pPos < 0 || pPos >= phongList.size || kPos < 0 || kPos >= khachThueList.size) return@setPositiveButton

                val selectedPhong = phongList[pPos]
                val selectedKhach = khachThueList[kPos]

                val ngayBD = etNgayBatDau.text.toString().trim()
                val ngayKT = etNgayKetThuc.text.toString().trim()
                val tienCocStr = etTienCoc.text.toString().trim()
                val giaThueStr = etGiaThue.text.toString().trim()
                val trangThai = etTrangThai.text.toString().trim().ifEmpty { "Hiệu lực" }

                val tienCoc = tienCocStr.toDoubleOrNull() ?: 0.0
                val giaThue = giaThueStr.toDoubleOrNull() ?: selectedPhong.giaThue

                if (hopDong == null) {
                    val newItem = HopDongEntity(
                        phongId = selectedPhong.id,
                        khachThueId = selectedKhach.id,
                        ngayBatDau = ngayBD,
                        ngayKetThuc = ngayKT,
                        tienCoc = tienCoc,
                        giaThue = giaThue,
                        trangThai = trangThai
                    )
                    hopDongViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm hợp đồng", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = hopDong.copy(
                        phongId = selectedPhong.id,
                        khachThueId = selectedKhach.id,
                        ngayBatDau = ngayBD,
                        ngayKetThuc = ngayKT,
                        tienCoc = tienCoc,
                        giaThue = giaThue,
                        trangThai = trangThai
                    )
                    hopDongViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật hợp đồng", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(hopDong: HopDongEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Hợp Đồng")
            .setMessage("Bạn có chắc chắn muốn xóa hợp đồng này?")
            .setPositiveButton("Xóa") { _, _ ->
                hopDongViewModel.delete(hopDong)
                Toast.makeText(this, "Đã xóa hợp đồng", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
