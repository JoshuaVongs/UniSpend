package com.example.unispend.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Add Goal::class to the entities list
// 2. Update version to 2
@Database(entities = [Expense::class, Goal::class], version = 2, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() { // <--- Class name matches your filename

    abstract fun expenseDao(): ExpenseDao
    abstract fun goalDao(): GoalDao // <--- Add this so we can save Goals

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java, // <--- Matches the class name
                    "expense_database"
                )
                    .fallbackToDestructiveMigration() // Wipes data cleanly to prevent crashes
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}