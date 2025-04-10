package com.example.ricktasks.data.repository

import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.entity.TaskEntity

class TaskRepository(
    private val taskDao: TaskDao
) {
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task:TaskEntity){
        taskDao.updateTask(task)
    }

    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }

    suspend fun deleteTask(task: TaskEntity){
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(id:Int) :TaskEntity{
        return taskDao.getTaskById(id)
    }

}