package com.example.ricktasks.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.data.repository.TaskRepository
import com.example.ricktasks.ui.fragments.tasks.AddEditTaskViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private var _tasks = MutableLiveData<List<TaskEntity>>()
    val tasks: LiveData<List<TaskEntity>> get() = _tasks

    fun getAllTasks() {
        viewModelScope.launch {
            _tasks.value = repository.getAllTasks()
        }
    }

    private val _task = MutableLiveData<TaskEntity>()
    val task: LiveData<TaskEntity> get() = _task

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}

class HomeViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = TaskRepository(taskDao)
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}