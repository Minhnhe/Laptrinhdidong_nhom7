package com.example.dack2026.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dack2026.R
import com.example.dack2026.data.entity.HoaDonEntity

class HoaDonAdapter(
    private val onEditClick: (HoaDonEntity) -> Unit,
    private val onDeleteClick: (HoaDonEntity) -> Unit
) : ListAdapter<HoaDonEntity, HoaDonAdapter.HoaDonViewHolder>(DIFF_CALLBACK) {

    class HoaDonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvThangNam: TextView = view.findViewById(R.id.tvThangNam)
        val tvTrangThai: TextView = view.findViewById(R.id.tvTrangThai)
        val tvPhongInfo: TextView = view.findViewById(R.id.tvPhongInfo)
        val tvChiTiet: TextView = view.findViewById(R.id.tvChiTiet)
        val tvTongTien: TextView = view.findViewById(R.id.tvTongTien)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoaDonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hoa_don, parent, false)
        return HoaDonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoaDonViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvThangNam.text = "Tháng: ${item.thangNam}"
        holder.tvTrangThai.text = item.trangThai
        holder.tvPhongInfo.text = "Hóa đơn phòng #${item.phongId}"
        val dienUsage = (item.chiSoDienMoi - item.chiSoDienCu).coerceAtLeast(0)
        val nuocUsage = (item.chiSoNuocMoi - item.chiSoNuocCu).coerceAtLeast(0)
        holder.tvChiTiet.text = "Điện: ${dienUsage} kWh - Nước: ${nuocUsage} m³ - Phòng: ${item.tienPhong.toInt()} đ"
        holder.tvTongTien.text = "Tổng tiền: ${item.tongTien.toInt()} đ"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<HoaDonEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HoaDonEntity>() {
            override fun areItemsTheSame(oldItem: HoaDonEntity, newItem: HoaDonEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HoaDonEntity, newItem: HoaDonEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
