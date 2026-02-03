package com.example.unispend.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unispend.MainActivity
import com.example.unispend.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val security = SecurityManager(this)
        val etPin = findViewById<EditText>(R.id.etPin)
        val btnAction = findViewById<Button>(R.id.btnLogin)
        val tvTitle = findViewById<TextView>(R.id.tvWelcomeTitle)
        val tvSubtitle = findViewById<TextView>(R.id.tvWelcomeSubtitle)

        // Switch UI based on whether a password exists
        if (security.isFirstTime()) {
            tvTitle.text = "Setup Guard"
            tvSubtitle.text = "Create a personal password to start"
            btnAction.text = "Create Password"
        } else {
            tvTitle.text = "Locked"
            tvSubtitle.text = "Enter password to access your funds"
            btnAction.text = "Unlock"
        }

        btnAction.setOnClickListener {
            val input = etPin.text.toString()

            if (input.isEmpty()) {
                etPin.error = "Required"
                return@setOnClickListener
            }

            if (security.isFirstTime()) {
                // Sign Up Path: Save the password for the single user
                security.savePassword(input)
                Toast.makeText(this, "Security Guard Active!", Toast.LENGTH_SHORT).show()
                startDashboard()
            } else {
                // Login Path: Verify existing password
                if (input == security.getPassword()) {
                    startDashboard()
                } else {
                    etPin.error = "Incorrect Password"
                    etPin.setText("")
                }
            }
        }
    }

    private fun startDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Prevents returning to this screen via back button
    }
}