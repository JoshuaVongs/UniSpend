package com.example.unispend.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goal_table ORDER BY id DESC")
    fun readAllGoals(): LiveData<List<Goal>>
}