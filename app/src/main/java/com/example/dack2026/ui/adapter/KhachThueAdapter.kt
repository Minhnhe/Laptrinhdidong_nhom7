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
import com.example.dack2026.data.entity.KhachThueEntity

class KhachThueAdapter(
    private val onEditClick: (KhachThueEntity) -> Unit,
    private val onDeleteClick: (KhachThueEntity) -> Unit
) : ListAdapter<KhachThueEntity, KhachThueAdapter.KhachThueViewHolder>(DIFF_CALLBACK) {

    class KhachThueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHoTen: TextView = view.findViewById(R.id.tvHoTen)
        val tvSdt: TextView = view.findViewById(R.id.tvSdt)
        val tvCmnd: TextView = view.findViewById(R.id.tvCmnd)
        val tvQueQuan: TextView = view.findViewById(R.id.tvQueQuan)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KhachThueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_khach_thue, parent, false)
        return KhachThueViewHolder(view)
    }

    override fun onBindViewHolder(holder: KhachThueViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvHoTen.text = item.hoTen
        holder.tvSdt.text = "SĐT: ${item.sdt}"
        holder.tvCmnd.text = "CMND/CCCD: ${item.cmnd}"
        holder.tvQueQuan.text = "Quê quán: ${item.queQuan}"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<KhachThueEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KhachThueEntity>() {
            override fun areItemsTheSame(oldItem: KhachThueEntity, newItem: KhachThueEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: KhachThueEntity, newItem: KhachThueEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
