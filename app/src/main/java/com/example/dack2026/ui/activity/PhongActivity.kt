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
import com.example.dack2026.data.entity.NhaTroEntity
import com.example.dack2026.data.entity.PhongEntity
import com.example.dack2026.ui.adapter.PhongAdapter
import com.example.dack2026.ui.viewmodel.PhongViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhongActivity : AppCompatActivity() {
    private val phongViewModel: PhongViewModel by viewModels()
    private lateinit var adapter: PhongAdapter
    private var nhaTroList: List<NhaTroEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phong)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Phòng trọ"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PhongAdapter(
            onEditClick = { phong -> showAddEditDialog(phong) },
            onDeleteClick = { phong -> confirmDelete(phong) }
        )
        recyclerView.adapter = adapter

        val filterNhaTroId = intent.getLongExtra("nhaTroId", -1L)

        phongViewModel.allPhong.observe(this) { list ->
            val filtered = if (filterNhaTroId > 0) list.filter { it.nhaTroId == filterNhaTroId } else list
            adapter.updateData(filtered)
        }

        val db = AppDatabase.getDatabase(this)
        db.nhaTroDao().getAllNhaTro().observe(this) { list ->
            nhaTroList = list
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

    private fun showAddEditDialog(phong: PhongEntity?) {
        if (nhaTroList.isEmpty()) {
            Toast.makeText(this, "Vui lòng tạo Nhà Trọ trước khi thêm Phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_phong, null)
        val spinnerNhaTro = dialogView.findViewById<Spinner>(R.id.spinnerNhaTro)
        val etTenPhong = dialogView.findViewById<EditText>(R.id.etTenPhong)
        val etDienTich = dialogView.findViewById<EditText>(R.id.etDienTich)
        val etGiaThue = dialogView.findViewById<EditText>(R.id.etGiaThue)
        val etTrangThai = dialogView.findViewById<EditText>(R.id.etTrangThai)
        val etMoTa = dialogView.findViewById<EditText>(R.id.etMoTa)

        val nhaTroNames = nhaTroList.map { it.tenNhaTro }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nhaTroNames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNhaTro.adapter = spinnerAdapter

        if (phong != null) {
            etTenPhong.setText(phong.tenPhong)
            etDienTich.setText(phong.dienTich.toString())
            etGiaThue.setText(phong.giaThue.toString())
            etTrangThai.setText(phong.trangThai)
            etMoTa.setText(phong.moTa)
            val index = nhaTroList.indexOfFirst { it.id == phong.nhaTroId }
            if (index >= 0) spinnerNhaTro.setSelection(index)
        } else {
            etTrangThai.setText("Trống")
        }

        AlertDialog.Builder(this)
            .setTitle(if (phong == null) "Thêm Phòng" else "Sửa Phòng")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val selectedIndex = spinnerNhaTro.selectedItemPosition
                if (selectedIndex < 0 || selectedIndex >= nhaTroList.size) return@setPositiveButton
                val selectedNhaTro = nhaTroList[selectedIndex]

                val ten = etTenPhong.text.toString().trim()
                val dienTichStr = etDienTich.text.toString().trim()
                val giaThueStr = etGiaThue.text.toString().trim()
                val trangThai = etTrangThai.text.toString().trim().ifEmpty { "Trống" }
                val moTa = etMoTa.text.toString().trim()

                if (ten.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val dienTich = dienTichStr.toDoubleOrNull() ?: 0.0
                val giaThue = giaThueStr.toDoubleOrNull() ?: selectedNhaTro.giaMacDinh

                if (phong == null) {
                    val newItem = PhongEntity(
                        nhaTroId = selectedNhaTro.id,
                        tenPhong = ten,
                        dienTich = dienTich,
                        giaThue = giaThue,
                        trangThai = trangThai,
                        moTa = moTa
                    )
                    phongViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm phòng", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = phong.copy(
                        nhaTroId = selectedNhaTro.id,
                        tenPhong = ten,
                        dienTich = dienTich,
                        giaThue = giaThue,
                        trangThai = trangThai,
                        moTa = moTa
                    )
                    phongViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật phòng", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(phong: PhongEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Phòng")
            .setMessage("Bạn có chắc chắn muốn xóa ${phong.tenPhong}?")
            .setPositiveButton("Xóa") { _, _ ->
                phongViewModel.delete(phong)
                Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
