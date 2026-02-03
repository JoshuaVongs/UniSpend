package com.example.unispend.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unispend.R
import com.example.unispend.data.Expense
import com.example.unispend.viewmodel.ExpenseViewModel

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        // Bind the Views from XML
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val rgType = findViewById<RadioGroup>(R.id.rgType)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val spinner = findViewById<Spinner>(R.id.spinnerCategory)

        // 1. Customized Categories for a Nigerian Student
        val categories = arrayOf(
            "Food & Provisions",     // Daily feeding
            "Transport",             // Shuttle, Keke, Bike
            "Data & Airtime",        // Essential for research
            "School & Handouts",     // Textbooks, dues, practical manuals
            "Project & Printing",    // Final year project expenses
            "Tithe & Offering",      // Religious obligations
            "Entertainment",         // Relaxation
            "Other"                  // Miscellaneous / Urgent 2k
        )

        // 2. Setup the Dropdown Adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.adapter = adapter

        // 3. Save Button Logic
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val amountString = etAmount.text.toString()

            // Determine Income vs Expense
            val type = if (rgType.checkedRadioButtonId == R.id.rbIncome) "INCOME" else "EXPENSE"

            // Safely get the category (default to "Other" if something goes wrong)
            val selectedCategory = if (spinner.selectedItem != null) spinner.selectedItem.toString() else "Other"

            if (title.isNotEmpty() && amountString.isNotEmpty()) {
                val amount = amountString.toDoubleOrNull()

                if (amount != null) {
                    // 4. Create the Expense Object (Now with DATE!)
                    val expense = Expense(
                        title = title,
                        amount = amount,
                        type = type,
                        category = selectedCategory,
                        date = System.currentTimeMillis() // Capture the exact time of entry
                    )

                    // Send to Database
                    viewModel.addExpense(expense)

                    // Close the screen
                    finish()
                } else {
                    Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}