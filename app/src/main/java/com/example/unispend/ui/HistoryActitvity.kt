package com.example.unispend.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unispend.R
import com.example.unispend.viewmodel.ExpenseViewModel
import com.google.android.material.chip.ChipGroup

class HistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // 1. Initialize RecyclerView and Adapter
        val recycler = findViewById<RecyclerView>(R.id.recyclerHistory)
        adapter = ExpenseAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        // 2. Initialize the Brain (ViewModel)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        // 3. Observe All Data from the database for real-time updates
        viewModel.readAllData.observe(this) { list ->
            // Default view: Show everything when data changes
            adapter.submitList(list)
        }

        // 4. Modern Filter Logic (Material 3 compliant)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupFilters)

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            // Get the first selected chip ID, or default to All if none selected
            val checkedId = checkedIds.firstOrNull() ?: R.id.chipAll

            val fullList = viewModel.readAllData.value ?: emptyList()

            // Apply filtering logic
            val filteredList = when (checkedId) {
                R.id.chipIncome -> fullList.filter { it.type == "INCOME" }
                R.id.chipExpense -> fullList.filter { it.type == "EXPENSE" }
                else -> fullList // This handles chipAll
            }

            // Update the list with an animation
            adapter.submitList(filteredList)
        }
    }
}