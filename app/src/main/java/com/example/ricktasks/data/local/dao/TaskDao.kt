package com.example.ricktasks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ricktasks.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task:TaskEntity)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id= :id")
    suspend fun getTaskById(id:Int): TaskEntity?

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}