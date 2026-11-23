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
import com.example.dack2026.data.entity.NhaTroEntity

class NhaTroAdapter(
    private val onEditClick: (NhaTroEntity) -> Unit,
    private val onDeleteClick: (NhaTroEntity) -> Unit
) : ListAdapter<NhaTroEntity, NhaTroAdapter.NhaTroViewHolder>(DIFF_CALLBACK) {

    inner class NhaTroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTenNhaTro: TextView = view.findViewById(R.id.tvTenNhaTro)
        val tvDiaChi: TextView = view.findViewById(R.id.tvDiaChi)
        val tvMoTa: TextView = view.findViewById(R.id.tvMoTa)
        val tvGiaMacDinh: TextView = view.findViewById(R.id.tvGiaMacDinh)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NhaTroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nha_tro, parent, false)
        return NhaTroViewHolder(view)
    }

    override fun onBindViewHolder(holder: NhaTroViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvTenNhaTro.text = item.tenNhaTro
        holder.tvDiaChi.text = "Địa chỉ: ${item.diaChi}"
        holder.tvMoTa.text = "Mô tả: ${item.moTa}"
        holder.tvGiaMacDinh.text = "Giá mặc định: ${item.giaMacDinh.toInt()} đ"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<NhaTroEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NhaTroEntity>() {
            override fun areItemsTheSame(oldItem: NhaTroEntity, newItem: NhaTroEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NhaTroEntity, newItem: NhaTroEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
