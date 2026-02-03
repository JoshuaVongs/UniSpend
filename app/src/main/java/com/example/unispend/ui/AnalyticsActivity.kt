package com.example.unispend.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unispend.R
import com.example.unispend.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        chart = findViewById(R.id.barChart)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        setupChartAppearance()

        viewModel.readAllData.observe(this) { transactions ->
            if (transactions.isEmpty()) {
                chart.clear()
                return@observe
            }

            val totalIncome = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }.toFloat()
            val totalExpense = transactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }.toFloat()

            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(0f, totalIncome))
            entries.add(BarEntry(1f, totalExpense))

            val dataSet = BarDataSet(entries, "Cash Flow")
            dataSet.colors = listOf(getColor(R.color.accent_green), getColor(R.color.error_red))
            dataSet.valueTextColor = Color.WHITE
            dataSet.valueTextSize = 14f

            val data = BarData(dataSet)
            data.barWidth = 0.5f

            chart.data = data
            chart.invalidate()
            chart.animateY(1000)
        }
    }

    private fun setupChartAppearance() {
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setNoDataText("Add transactions to see trends")
        chart.setNoDataTextColor(Color.WHITE)

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.WHITE
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Income", "Expense"))
        xAxis.granularity = 1f

        chart.axisLeft.textColor = Color.WHITE
        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.isEnabled = false
    }
}