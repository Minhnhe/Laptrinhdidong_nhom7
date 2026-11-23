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
import com.example.dack2026.data.entity.PhongEntity

class PhongAdapter(
    private val onEditClick: (PhongEntity) -> Unit,
    private val onDeleteClick: (PhongEntity) -> Unit
) : ListAdapter<PhongEntity, PhongAdapter.PhongViewHolder>(DIFF_CALLBACK) {

    class PhongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTenPhong: TextView = view.findViewById(R.id.tvTenPhong)
        val tvTrangThai: TextView = view.findViewById(R.id.tvTrangThai)
        val tvDienTich: TextView = view.findViewById(R.id.tvDienTich)
        val tvGiaThue: TextView = view.findViewById(R.id.tvGiaThue)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_phong, parent, false)
        return PhongViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhongViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvTenPhong.text = item.tenPhong
        holder.tvTrangThai.text = item.trangThai
        holder.tvDienTich.text = "Diện tích: ${item.dienTich} m²"
        holder.tvGiaThue.text = "Giá thuê: ${item.giaThue.toInt()} đ"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<PhongEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhongEntity>() {
            override fun areItemsTheSame(oldItem: PhongEntity, newItem: PhongEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PhongEntity, newItem: PhongEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
