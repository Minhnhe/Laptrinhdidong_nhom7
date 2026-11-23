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
import com.example.dack2026.data.entity.HoaDonEntity
import com.example.dack2026.data.entity.PhongEntity
import com.example.dack2026.ui.adapter.HoaDonAdapter
import com.example.dack2026.ui.viewmodel.HoaDonViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HoaDonActivity : AppCompatActivity() {
    private val hoaDonViewModel: HoaDonViewModel by viewModels()
    private lateinit var adapter: HoaDonAdapter
    private var phongList: List<PhongEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hoa_don)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Hóa đơn"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HoaDonAdapter(
            onEditClick = { hoaDon -> showAddEditDialog(hoaDon) },
            onDeleteClick = { hoaDon -> confirmDelete(hoaDon) }
        )
        recyclerView.adapter = adapter

        hoaDonViewModel.allHoaDon.observe(this) { list ->
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

    private fun showAddEditDialog(hoaDon: HoaDonEntity?) {
        if (phongList.isEmpty()) {
            Toast.makeText(this, "Vui lòng tạo Phòng trước khi lập Hóa Đơn", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_hoa_don, null)
        val spinnerPhong = dialogView.findViewById<Spinner>(R.id.spinnerPhong)
        val etThangNam = dialogView.findViewById<EditText>(R.id.etThangNam)
        val etChiSoDienCu = dialogView.findViewById<EditText>(R.id.etChiSoDienCu)
        val etChiSoDienMoi = dialogView.findViewById<EditText>(R.id.etChiSoDienMoi)
        val etDonGiaDien = dialogView.findViewById<EditText>(R.id.etDonGiaDien)
        val etChiSoNuocCu = dialogView.findViewById<EditText>(R.id.etChiSoNuocCu)
        val etChiSoNuocMoi = dialogView.findViewById<EditText>(R.id.etChiSoNuocMoi)
        val etDonGiaNuoc = dialogView.findViewById<EditText>(R.id.etDonGiaNuoc)
        val etTienPhong = dialogView.findViewById<EditText>(R.id.etTienPhong)
        val etTienDichVu = dialogView.findViewById<EditText>(R.id.etTienDichVu)
        val etTrangThai = dialogView.findViewById<EditText>(R.id.etTrangThai)

        val phongNames = phongList.map { it.tenPhong }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, phongNames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPhong.adapter = spinnerAdapter

        if (hoaDon != null) {
            etThangNam.setText(hoaDon.thangNam)
            etChiSoDienCu.setText(hoaDon.chiSoDienCu.toString())
            etChiSoDienMoi.setText(hoaDon.chiSoDienMoi.toString())
            etDonGiaDien.setText(hoaDon.donGiaDien.toString())
            etChiSoNuocCu.setText(hoaDon.chiSoNuocCu.toString())
            etChiSoNuocMoi.setText(hoaDon.chiSoNuocMoi.toString())
            etDonGiaNuoc.setText(hoaDon.donGiaNuoc.toString())
            etTienPhong.setText(hoaDon.tienPhong.toString())
            etTienDichVu.setText(hoaDon.tienDichVu.toString())
            etTrangThai.setText(hoaDon.trangThai)

            val index = phongList.indexOfFirst { it.id == hoaDon.phongId }
            if (index >= 0) spinnerPhong.setSelection(index)
        } else {
            etThangNam.setText("06/2026")
            etChiSoDienCu.setText("0")
            etChiSoDienMoi.setText("50")
            etDonGiaDien.setText("3500")
            etChiSoNuocCu.setText("0")
            etChiSoNuocMoi.setText("10")
            etDonGiaNuoc.setText("10000")
            etTienPhong.setText(phongList[0].giaThue.toString())
            etTienDichVu.setText("100000")
            etTrangThai.setText("Chưa thanh toán")
        }

        AlertDialog.Builder(this)
            .setTitle(if (hoaDon == null) "Thêm Hóa Đơn" else "Sửa Hóa Đơn")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val pPos = spinnerPhong.selectedItemPosition
                if (pPos < 0 || pPos >= phongList.size) return@setPositiveButton
                val selectedPhong = phongList[pPos]

                val thangNam = etThangNam.text.toString().trim()
                val dienCu = etChiSoDienCu.text.toString().toIntOrNull() ?: 0
                val dienMoi = etChiSoDienMoi.text.toString().toIntOrNull() ?: 0
                val donGiaDien = etDonGiaDien.text.toString().toDoubleOrNull() ?: 3500.0

                val nuocCu = etChiSoNuocCu.text.toString().toIntOrNull() ?: 0
                val nuocMoi = etChiSoNuocMoi.text.toString().toIntOrNull() ?: 0

                if (dienMoi < dienCu || nuocMoi < nuocCu) {
                    Toast.makeText(this, "Chỉ số điện/nước mới phải lớn hơn hoặc bằng chỉ số cũ", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val donGiaNuoc = etDonGiaNuoc.text.toString().toDoubleOrNull() ?: 10000.0

                val tienPhong = etTienPhong.text.toString().toDoubleOrNull() ?: selectedPhong.giaThue
                val tienDichVu = etTienDichVu.text.toString().toDoubleOrNull() ?: 0.0
                val trangThai = etTrangThai.text.toString().trim().ifEmpty { "Chưa thanh toán" }

                val tienDien = (dienMoi - dienCu) * donGiaDien
                val tienNuoc = (nuocMoi - nuocCu) * donGiaNuoc
                val tongTien = tienDien + tienNuoc + tienPhong + tienDichVu

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val ngayTao = dateFormat.format(Date())

                if (hoaDon == null) {
                    val newItem = HoaDonEntity(
                        phongId = selectedPhong.id,
                        thangNam = thangNam,
                        chiSoDienCu = dienCu,
                        chiSoDienMoi = dienMoi,
                        donGiaDien = donGiaDien,
                        chiSoNuocCu = nuocCu,
                        chiSoNuocMoi = nuocMoi,
                        donGiaNuoc = donGiaNuoc,
                        tienPhong = tienPhong,
                        tienDichVu = tienDichVu,
                        tongTien = tongTien,
                        trangThai = trangThai,
                        ngayTao = ngayTao
                    )
                    hoaDonViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm hóa đơn (Tổng: $tongTien đ)", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = hoaDon.copy(
                        phongId = selectedPhong.id,
                        thangNam = thangNam,
                        chiSoDienCu = dienCu,
                        chiSoDienMoi = dienMoi,
                        donGiaDien = donGiaDien,
                        chiSoNuocCu = nuocCu,
                        chiSoNuocMoi = nuocMoi,
                        donGiaNuoc = donGiaNuoc,
                        tienPhong = tienPhong,
                        tienDichVu = tienDichVu,
                        tongTien = tongTien,
                        trangThai = trangThai
                    )
                    hoaDonViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật hóa đơn (Tổng: $tongTien đ)", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(hoaDon: HoaDonEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Hóa Đơn")
            .setMessage("Bạn có chắc chắn muốn xóa hóa đơn này?")
            .setPositiveButton("Xóa") { _, _ ->
                hoaDonViewModel.delete(hoaDon)
                Toast.makeText(this, "Đã xóa hóa đơn", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
