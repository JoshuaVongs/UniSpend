package com.example.unispend.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExpense(expense: Expense)

    // --- THIS WAS MISSING ---
    @Update
    suspend fun updateExpense(expense: Expense)

    // --- THIS WAS MISSING ---
    @Delete
    suspend fun deleteExpense(expense: Expense)

    // --- THIS WAS MISSING (Used for wiping data) ---
    @Query("DELETE FROM expense_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM expense_table ORDER BY date DESC") // Orders by newest date
    fun readAllData(): LiveData<List<Expense>>
}