package com.example.unispend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.unispend.data.Expense
import com.example.unispend.data.ExpenseDatabase
import com.example.unispend.data.ExpenseRepository
import com.example.unispend.data.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Expense>>
    val readAllGoals: LiveData<List<Goal>>
    private val repository: ExpenseRepository

    init {
        val database = ExpenseDatabase.getDatabase(application)
        val expenseDao = database.expenseDao()
        val goalDao = database.goalDao()
        repository = ExpenseRepository(expenseDao, goalDao)
        readAllData = repository.readAllData
        readAllGoals = repository.readAllGoals
    }

    // --- EXPENSES ---
    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) { repository.addExpense(expense) }
    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) { repository.updateExpense(expense) }
    fun deleteExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) { repository.deleteExpense(expense) }
    fun wipeAllData() = viewModelScope.launch(Dispatchers.IO) { repository.deleteAll() }

    // --- GOALS ---
    fun addGoal(goal: Goal) = viewModelScope.launch(Dispatchers.IO) { repository.addGoal(goal) }
    fun updateGoal(goal: Goal) = viewModelScope.launch(Dispatchers.IO) { repository.updateGoal(goal) }
    fun deleteGoal(goal: Goal) = viewModelScope.launch(Dispatchers.IO) { repository.deleteGoal(goal) }
}