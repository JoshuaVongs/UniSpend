package com.example.unispend.ui

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unispend.R
import com.example.unispend.data.Goal
import com.example.unispend.viewmodel.ExpenseViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SavingsActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: GoalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.rvGoals)
        adapter = GoalAdapter { goal -> showEditDialog(goal) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.readAllGoals.observe(this) { goals -> adapter.submitList(goals) }

        findViewById<ExtendedFloatingActionButton>(R.id.fabAddGoal).setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Goal")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val inputTitle = EditText(this)
        inputTitle.hint = "Goal Name"
        layout.addView(inputTitle)

        val inputTarget = EditText(this)
        inputTarget.hint = "Target Amount"
        inputTarget.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(inputTarget)

        builder.setView(layout)
        builder.setPositiveButton("Save") { _, _ ->
            val title = inputTitle.text.toString()
            val target = inputTarget.text.toString().toDoubleOrNull() ?: 0.0
            if (title.isNotEmpty()) {
                viewModel.addGoal(Goal(title = title, targetAmount = target, savedAmount = 0.0))
            }
        }
        builder.setNegativeButton("Cancel") { d, _ -> d.dismiss() }
        builder.show()
    }

    private fun showEditDialog(goal: Goal) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update: ${goal.title}")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val inputSaved = EditText(this)
        inputSaved.hint = "Total Saved Amount"
        inputSaved.inputType = InputType.TYPE_CLASS_NUMBER
        inputSaved.setText(goal.savedAmount.toInt().toString())
        layout.addView(inputSaved)

        builder.setView(layout)
        builder.setPositiveButton("Update") { _, _ ->
            val newSaved = inputSaved.text.toString().toDoubleOrNull() ?: goal.savedAmount
            viewModel.updateGoal(goal.copy(savedAmount = newSaved))
        }
        builder.setNeutralButton("Delete") { _, _ -> viewModel.deleteGoal(goal) }
        builder.show()
    }
}