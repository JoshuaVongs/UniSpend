package com.example.unispend.ui

import android.content.Context

class SecurityManager(context: Context) {
    // SharedPrefs is perfect for a single-user personal app
    private val prefs = context.getSharedPreferences("UniSpendSecure", Context.MODE_PRIVATE)

    fun savePassword(password: String) {
        prefs.edit().putString("APP_PASSWORD", password).apply()
    }

    fun getPassword(): String? {
        return prefs.getString("APP_PASSWORD", null)
    }

    fun isFirstTime(): Boolean {
        // If this returns true, we show the "Sign Up" mode
        return getPassword() == null
    }
}