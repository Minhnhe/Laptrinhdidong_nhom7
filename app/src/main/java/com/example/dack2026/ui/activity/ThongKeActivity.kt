package com.example.dack2026.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dack2026.R
import com.example.dack2026.ui.viewmodel.ThongKeViewModel

class ThongKeActivity : AppCompatActivity() {
    private val viewModel: ThongKeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thong_ke)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { finish() }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Thống kê Doanh thu"

        val tvRevenue = findViewById<TextView>(R.id.tvRevenue)
        val tvThu = findViewById<TextView>(R.id.tvThu)
        val tvChi = findViewById<TextView>(R.id.tvChi)
        val tvProfit = findViewById<TextView>(R.id.tvProfit)

        viewModel.totalRevenue.observe(this) { rev ->
            val thu = viewModel.totalThu.value ?: 0.0
            val chi = viewModel.totalChi.value ?: 0.0
            val profit = (rev ?: 0.0) + thu - chi

            tvRevenue.text = "${rev ?: 0.0} đ"
            tvThu.text = "$thu đ"
            tvChi.text = "$chi đ"
            tvProfit.text = "$profit đ"
            updateChart(rev ?: 0.0, thu, chi)
        }

        viewModel.totalThu.observe(this) { thu ->
            val rev = viewModel.totalRevenue.value ?: 0.0
            val chi = viewModel.totalChi.value ?: 0.0
            val profit = rev + (thu ?: 0.0) - chi

            tvRevenue.text = "$rev đ"
            tvThu.text = "${thu ?: 0.0} đ"
            tvChi.text = "$chi đ"
            tvProfit.text = "$profit đ"
            updateChart(rev, thu ?: 0.0, chi)
        }

        viewModel.totalChi.observe(this) { chi ->
            val rev = viewModel.totalRevenue.value ?: 0.0
            val thu = viewModel.totalThu.value ?: 0.0
            val profit = rev + thu - (chi ?: 0.0)

            tvRevenue.text = "$rev đ"
            tvThu.text = "$thu đ"
            tvChi.text = "${chi ?: 0.0} đ"
            tvProfit.text = "$profit đ"
            updateChart(rev, thu, chi ?: 0.0)
        }

        viewModel.loadStatistics()
    }

    private fun updateChart(rev: Double, thu: Double, chi: Double) {
        val totalIn = rev + thu
        val progressBarChart = findViewById<android.widget.ProgressBar>(R.id.progressBarChart)
        val tvChartInfo = findViewById<TextView>(R.id.tvChartInfo)

        if (totalIn <= 0.0 && chi <= 0.0) {
            progressBarChart.progress = 50
            tvChartInfo.text = "Chưa có dữ liệu thu chi để vẽ biểu đồ tỷ trọng."
            return
        }

        val total = totalIn + chi
        val percentIn = if (total > 0) ((totalIn / total) * 100).toInt() else 50
        progressBarChart.progress = percentIn
        tvChartInfo.text = "Tỷ trọng Dòng tiền Thu: $percentIn% | Tỷ trọng Dòng tiền Chi: ${100 - percentIn}%"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStatistics()
    }
}
