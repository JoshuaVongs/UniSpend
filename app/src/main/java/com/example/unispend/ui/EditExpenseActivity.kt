package com.example.unispend.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.unispend.R
import com.google.android.material.textfield.TextInputEditText

class EditExpenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)

        // 1. Get the data passed from the Adapter
        val title = intent.getStringExtra("EXPENSE_TITLE") ?: "Unknown"
        val amount = intent.getDoubleExtra("EXPENSE_AMOUNT", 0.0)
        val type = intent.getStringExtra("EXPENSE_TYPE") ?: "EXPENSE"

        // 2. Link the Views
        val etTitle = findViewById<TextInputEditText>(R.id.etEditTitle)
        val etAmount = findViewById<TextInputEditText>(R.id.etEditAmount)
        val btnBack = findViewById<Button>(R.id.btnGoBack)
        val tvHeader = findViewById<TextView>(R.id.tvDetailHeader)

        // 3. Set the data
        etTitle.setText(title)
        etAmount.setText(amount.toString())

        // Make fields "Read Only" to enforce accountability (cannot delete/edit history)
        etTitle.isEnabled = false
        etAmount.isEnabled = false

        // Update header color based on type
        if (type == "INCOME") {
            tvHeader.text = "Income Record"
            tvHeader.setTextColor(getColor(R.color.accent_green))
        } else {
            tvHeader.text = "Expense Record"
            tvHeader.setTextColor(getColor(R.color.error_red))
        }

        // 4. Close button
        btnBack.setOnClickListener {
            finish()
        }
    }
}