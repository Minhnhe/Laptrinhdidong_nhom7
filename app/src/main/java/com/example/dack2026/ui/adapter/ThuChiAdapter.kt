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
import com.example.dack2026.data.entity.ThuChiEntity

class ThuChiAdapter(
    private val onEditClick: (ThuChiEntity) -> Unit,
    private val onDeleteClick: (ThuChiEntity) -> Unit
) : ListAdapter<ThuChiEntity, ThuChiAdapter.ThuChiViewHolder>(DIFF_CALLBACK) {

    class ThuChiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNoiDung: TextView = view.findViewById(R.id.tvNoiDung)
        val tvLoai: TextView = view.findViewById(R.id.tvLoai)
        val tvSoTien: TextView = view.findViewById(R.id.tvSoTien)
        val tvNgay: TextView = view.findViewById(R.id.tvNgay)
        val tvGhiChu: TextView = view.findViewById(R.id.tvGhiChu)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThuChiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thu_chi, parent, false)
        return ThuChiViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThuChiViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvNoiDung.text = item.noiDung
        holder.tvLoai.text = item.loai
        holder.tvSoTien.text = "Số tiền: ${item.soTien.toInt()} đ"
        holder.tvNgay.text = "Ngày: ${item.ngay}"
        holder.tvGhiChu.text = "Ghi chú: ${item.ghiChu.ifEmpty { "Không có" }}"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun updateData(newList: List<ThuChiEntity>) {
        submitList(newList)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ThuChiEntity>() {
            override fun areItemsTheSame(oldItem: ThuChiEntity, newItem: ThuChiEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ThuChiEntity, newItem: ThuChiEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
