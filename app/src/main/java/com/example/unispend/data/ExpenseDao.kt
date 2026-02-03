package com.example.unispend.data

import androidx.lifecycle.LiveData

class ExpenseRepository(private val expenseDao: ExpenseDao, private val goalDao: GoalDao) {

    // --- EXPENSES ---
    val readAllData: LiveData<List<Expense>> = expenseDao.readAllData()

    suspend fun addExpense(expense: Expense) { expenseDao.addExpense(expense) }
    suspend fun updateExpense(expense: Expense) { expenseDao.updateExpense(expense) }
    suspend fun deleteExpense(expense: Expense) { expenseDao.deleteExpense(expense) }
    suspend fun deleteAll() { expenseDao.deleteAll() }

    // --- GOALS ---
    val readAllGoals: LiveData<List<Goal>> = goalDao.readAllGoals()

    suspend fun addGoal(goal: Goal) { goalDao.addGoal(goal) }
    suspend fun updateGoal(goal: Goal) { goalDao.updateGoal(goal) }
    suspend fun deleteGoal(goal: Goal) { goalDao.deleteGoal(goal) }
}