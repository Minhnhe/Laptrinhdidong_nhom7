package com.example.dack2026.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dack2026.R
import com.example.dack2026.data.entity.NhaTroEntity
import com.example.dack2026.ui.adapter.NhaTroAdapter
import com.example.dack2026.ui.viewmodel.NhaTroViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NhaTroActivity : AppCompatActivity() {
    private val nhaTroViewModel: NhaTroViewModel by viewModels()
    private lateinit var adapter: NhaTroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nha_tro)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Quản lý Nhà trọ"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NhaTroAdapter(
            onEditClick = { nhaTro -> showAddEditDialog(nhaTro) },
            onDeleteClick = { nhaTro -> confirmDelete(nhaTro) }
        )
        recyclerView.adapter = adapter

        nhaTroViewModel.allNhaTro.observe(this) { list ->
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

    private fun showAddEditDialog(nhaTro: NhaTroEntity?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nha_tro, null)
        val etTen = dialogView.findViewById<EditText>(R.id.etTenNhaTro)
        val etDiaChi = dialogView.findViewById<EditText>(R.id.etDiaChi)
        val etMoTa = dialogView.findViewById<EditText>(R.id.etMoTa)
        val etGia = dialogView.findViewById<EditText>(R.id.etGiaMacDinh)

        if (nhaTro != null) {
            etTen.setText(nhaTro.tenNhaTro)
            etDiaChi.setText(nhaTro.diaChi)
            etMoTa.setText(nhaTro.moTa)
            etGia.setText(nhaTro.giaMacDinh.toString())
        }

        AlertDialog.Builder(this)
            .setTitle(if (nhaTro == null) "Thêm Nhà Trọ" else "Sửa Nhà Trọ")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val ten = etTen.text.toString().trim()
                val diaChi = etDiaChi.text.toString().trim()
                val moTa = etMoTa.text.toString().trim()
                val giaStr = etGia.text.toString().trim()

                if (ten.isEmpty() || diaChi.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên và địa chỉ", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val gia = giaStr.toDoubleOrNull() ?: 0.0

                if (nhaTro == null) {
                    val newItem = NhaTroEntity(tenNhaTro = ten, diaChi = diaChi, moTa = moTa, giaMacDinh = gia)
                    nhaTroViewModel.insert(newItem)
                    Toast.makeText(this, "Đã thêm nhà trọ", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedItem = nhaTro.copy(tenNhaTro = ten, diaChi = diaChi, moTa = moTa, giaMacDinh = gia)
                    nhaTroViewModel.update(updatedItem)
                    Toast.makeText(this, "Đã cập nhật nhà trọ", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(nhaTro: NhaTroEntity) {
        AlertDialog.Builder(this)
            .setTitle("Xóa Nhà Trọ")
            .setMessage("Bạn có chắc chắn muốn xóa ${nhaTro.tenNhaTro}?")
            .setPositiveButton("Xóa") { _, _ ->
                nhaTroViewModel.delete(nhaTro)
                Toast.makeText(this, "Đã xóa nhà trọ", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
