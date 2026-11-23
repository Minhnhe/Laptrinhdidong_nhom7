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
            onDeleteClick = { hoaDon -> confirmDelete(hoaDon) },
            onExportPdfClick = { hoaDon -> exportHoaDonToPdf(hoaDon) }
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
        val etTienWifi = dialogView.findViewById<EditText>(R.id.etTienWifi)
        val etTienRac = dialogView.findViewById<EditText>(R.id.etTienRac)
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
            etTienWifi.setText(hoaDon.tienWifi.toString())
            etTienRac.setText(hoaDon.tienRac.toString())
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
            etTienWifi.setText("50000")
            etTienRac.setText("30000")
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
                val tienWifi = etTienWifi.text.toString().toDoubleOrNull() ?: 50000.0
                val tienRac = etTienRac.text.toString().toDoubleOrNull() ?: 30000.0
                val trangThai = etTrangThai.text.toString().trim().ifEmpty { "Chưa thanh toán" }

                val tienDien = (dienMoi - dienCu) * donGiaDien
                val tienNuoc = (nuocMoi - nuocCu) * donGiaNuoc
                val tongTien = tienDien + tienNuoc + tienPhong + tienWifi + tienRac

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
                        tienWifi = tienWifi,
                        tienRac = tienRac,
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
                        tienWifi = tienWifi,
                        tienRac = tienRac,
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

    private fun exportHoaDonToPdf(hoaDon: HoaDonEntity) {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 14f
        }
        val titlePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 20f
            isFakeBoldText = true
        }

        var y = 50f
        canvas.drawText("HÓA ĐƠN TIỀN NHÀ TRỌ", 180f, y, titlePaint)
        y += 40f
        paint.textSize = 14f
        canvas.drawText("Tháng/Năm: ${hoaDon.thangNam}", 50f, y, paint)
        y += 30f
        canvas.drawText("Mã Phòng: ${hoaDon.phongId}", 50f, y, paint)
        y += 30f
        canvas.drawText("Ngày tạo: ${hoaDon.ngayTao}", 50f, y, paint)
        y += 40f

        canvas.drawText("----------------------------------------------------------------", 50f, y, paint)
        y += 30f

        val dienUsage = (hoaDon.chiSoDienMoi - hoaDon.chiSoDienCu).coerceAtLeast(0)
        val nuocUsage = (hoaDon.chiSoNuocMoi - hoaDon.chiSoNuocCu).coerceAtLeast(0)
        val tienDien = dienUsage * hoaDon.donGiaDien
        val tienNuoc = nuocUsage * hoaDon.donGiaNuoc

        canvas.drawText("1. Tiền phòng: ${hoaDon.tienPhong.toInt()} đ", 50f, y, paint)
        y += 25f
        canvas.drawText("2. Tiền điện ($dienUsage kWh x ${hoaDon.donGiaDien}): ${tienDien.toInt()} đ", 50f, y, paint)
        y += 25f
        canvas.drawText("3. Tiền nước ($nuocUsage m³ x ${hoaDon.donGiaNuoc}): ${tienNuoc.toInt()} đ", 50f, y, paint)
        y += 25f
        canvas.drawText("4. Tiền WiFi: ${hoaDon.tienWifi.toInt()} đ", 50f, y, paint)
        y += 25f
        canvas.drawText("5. Tiền rác: ${hoaDon.tienRac.toInt()} đ", 50f, y, paint)
        y += 35f

        canvas.drawText("----------------------------------------------------------------", 50f, y, paint)
        y += 30f
        val boldPaint = android.graphics.Paint(titlePaint).apply { textSize = 18f; color = android.graphics.Color.RED }
        canvas.drawText("TỔNG TIỀN: ${hoaDon.tongTien.toInt()} đ", 50f, y, boldPaint)
        y += 40f
        canvas.drawText("Trạng thái: ${hoaDon.trangThai}", 50f, y, paint)

        pdfDocument.finishPage(page)

        val file = java.io.File(getExternalFilesDir(null), "HoaDon_${hoaDon.thangNam.replace("/", "_")}_Phong_${hoaDon.phongId}.pdf")
        try {
            pdfDocument.writeTo(java.io.FileOutputStream(file))
            Toast.makeText(this, "Đã xuất PDF thành công!\nĐường dẫn: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi xuất PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}
