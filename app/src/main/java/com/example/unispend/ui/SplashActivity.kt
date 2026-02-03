package com.example.unispend.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.unispend.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Using a Handler to pause for 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Transition to the WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)

            // Finish prevents the user from going back to the splash screen
            finish()
        }, 3000)
    }
}