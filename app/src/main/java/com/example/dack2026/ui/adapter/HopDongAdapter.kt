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
import com.example.dack2026.data.entity.HopDongEntity

class HopDongAdapter(
    private val onEditClick: (HopDongEntity) -> Unit,
    private val onDeleteClick: (HopDongEntity) -> Unit
) : ListAdapter<HopDongEntity, HopDongAdapter.HopDongViewHolder>(DIFF_CALLBACK) {

    class HopDongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhongInfo: TextView = view.findViewById(R.id.tvPhongInfo)
        val tvTrangThai: TextView = view.findViewById(R.id.tvTrangThai)
        val tvKhachThueInfo: TextView = view.findViewById(R.id.tvKhachThueInfo)
        val tvThoiGian: TextView = view.findViewById(R.id.tvThoiGian)
        val tvTienCoc: TextView = view.findViewById(R.id.tvTienCoc)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HopDongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hop_dong, parent, false)
        return HopDongViewHolder(view)
    }

    override fun onBindViewHolder(holder: HopDongViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvPhongInfo.text = "Hợp đồng phòng #${item.phongId}"
        holder.tvTrangThai.text = item.trangThai
        holder.tvKhachThueInfo.text = "Khách thuê #${item.khachThueId}"
        holder.tvThoiGian.text = "Thời gian: ${item.ngayBatDau} - ${item.ngayKetThuc}"
        holder.tvTienCoc.text = "Tiền cọc: ${item.tienCoc.toInt()} đ"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<HopDongEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HopDongEntity>() {
            override fun areItemsTheSame(oldItem: HopDongEntity, newItem: HopDongEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HopDongEntity, newItem: HopDongEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
