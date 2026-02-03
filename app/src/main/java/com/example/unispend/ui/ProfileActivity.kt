package com.example.unispend.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unispend.MainActivity
import com.example.unispend.R
import com.example.unispend.viewmodel.ExpenseViewModel
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        // Bind Views
        val etName = findViewById<TextInputEditText>(R.id.etProfileName)
        val etSchool = findViewById<TextInputEditText>(R.id.etProfileSchool)
        val etLevel = findViewById<TextInputEditText>(R.id.etProfileLevel)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)
        val btnReset = findViewById<Button>(R.id.btnResetData)

        // --- PART 1: LOAD SAVED DATA ---
        val prefs = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        // Defaults: "Joshua", "University of Nigeria", "400L"
        etName.setText(prefs.getString("USER_NAME", "Joshua"))
        etSchool.setText(prefs.getString("USER_SCHOOL", "University Name"))
        etLevel.setText(prefs.getString("USER_LEVEL", "Level"))

        // --- PART 2: SAVE BUTTON ---
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val school = etSchool.text.toString()
            val level = etLevel.text.toString()

            if (name.isNotEmpty() && school.isNotEmpty() && level.isNotEmpty()) {
                prefs.edit().apply {
                    putString("USER_NAME", name)
                    putString("USER_SCHOOL", school)
                    putString("USER_LEVEL", level)
                    apply()
                }
                Toast.makeText(this, "Student Profile Updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // --- PART 3: RESET DATA ---
        btnReset.setOnClickListener {
            showResetConfirmation()
        }
    }

    private fun showResetConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Wipe")
        builder.setMessage("This will delete all your income and expense records permanently.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes, Wipe") { _, _ ->
            viewModel.wipeAllData()
            Toast.makeText(this, "Records Cleared.", Toast.LENGTH_SHORT).show()

            // Restart app to clear dashboard
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}