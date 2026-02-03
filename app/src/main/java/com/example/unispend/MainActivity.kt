package com.example.unispend

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unispend.ui.*
import com.example.unispend.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var pieChart: PieChart
    private lateinit var tvWelcome: TextView
    private lateinit var tvTotalBalance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        pieChart = findViewById(R.id.pieChart)
        tvWelcome = findViewById(R.id.tvWelcome) // Make sure you have this ID in XML
        tvTotalBalance = findViewById(R.id.tvTotalBalance)

        // 2. Configure Basic Chart Styling (The "Donut" Look)
        pieChart.description.isEnabled = false
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(Color.parseColor("#121212")) // Matches Dark Background
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.setCenterTextSize(14f)

        // Disable interaction to keep it static and clean
        pieChart.setTouchEnabled(true)
        pieChart.rotationAngle = 0f

        // 3. Setup Navigation Buttons
        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        findViewById<Button>(R.id.btnAnalytics).setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }
        findViewById<Button>(R.id.btnSavings).setOnClickListener {
            startActivity(Intent(this, SavingsActivity::class.java))
        }
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<ExtendedFloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        // 4. Live Data Observer (Updates Balance & Chart automatically)
        viewModel.readAllData.observe(this) { expenses ->
            // Calculate Total Balance (Income - Expense)
            val total = expenses.sumOf { if(it.type == "INCOME") it.amount else -it.amount }
            tvTotalBalance.text = "₦ %,.2f".format(total)

            // Refresh the Chart
            updateChartContent(expenses)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the Welcome Name when returning from Profile Screen
        val prefs = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val name = prefs.getString("USER_NAME", "Student")
        tvWelcome.text = "Welcome, $name"
    }

    private fun updateChartContent(expenses: List<com.example.unispend.data.Expense>) {
        // We only chart EXPENSES (Spending)
        val expenseList = expenses.filter { it.type == "EXPENSE" }

        // Handle Empty State
        if (expenseList.isEmpty()) {
            pieChart.clear()
            pieChart.setNoDataText("No expenses recorded yet")
            pieChart.setNoDataTextColor(Color.WHITE)
            return
        }

        // 1. Group Data by Category
        val categoryMap = expenseList.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        // 2. Assign Specific Student Colors
        for ((category, amount) in categoryMap) {
            entries.add(PieEntry(amount, category))

            when (category) {
                "Food & Provisions" -> colors.add(Color.parseColor("#FFB300"))   // Warm Yellow
                "Transport" -> colors.add(Color.parseColor("#4CAF50"))           // Green
                "Data & Airtime" -> colors.add(Color.parseColor("#9C27B0"))      // Purple
                "School & Handouts" -> colors.add(Color.parseColor("#2196F3"))   // Blue
                "Project & Printing" -> colors.add(Color.parseColor("#FF5722"))  // Orange (Urgent)
                "Tithe & Offering" -> colors.add(Color.parseColor("#795548"))    // Brown
                "Entertainment" -> colors.add(Color.parseColor("#E91E63"))       // Pink
                else -> colors.add(Color.parseColor("#757575"))                  // Grey
            }
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.sliceSpace = 3f

        // --- CLEAN UP: Hide text on the chart itself ---
        dataSet.setDrawValues(false)
        pieChart.setDrawEntryLabels(false)

        // --- THE LEGEND (KEY) CONFIGURATION ---
        val legend = pieChart.legend
        legend.isEnabled = true
        legend.textColor = Color.WHITE
        legend.textSize = 12f
        legend.form = Legend.LegendForm.CIRCLE
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 10f // Space between chart and text
        legend.yEntrySpace = 5f  // Space between list items

        // 3. Render Data
        val data = PieData(dataSet)
        pieChart.data = data

        // Show Total Expense in Center
        val totalExpense = expenseList.sumOf { it.amount }
        pieChart.centerText = "Total Spent\n₦${"%,.0f".format(totalExpense)}"

        pieChart.animateY(1200) // Smooth animation
        pieChart.invalidate()
    }
}