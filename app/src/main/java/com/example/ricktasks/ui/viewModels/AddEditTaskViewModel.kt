package com.example.ricktasks.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.data.repository.TaskRepository
import kotlinx.coroutines.launch

class AddEditTaskViewModel(
    private val repository: TaskRepository
): ViewModel() {

    private val _task = MutableLiveData<TaskEntity>()
    val task: LiveData<TaskEntity> get() = _task

    private val _taskInserted = MutableLiveData<Boolean>()
    val taskInserted: LiveData<Boolean> get() = _taskInserted

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            try {
                // Intentar insertar la tarea
                repository.insertTask(task)
                _taskInserted.value = true
            } catch (e: Exception) {
                _taskInserted.value = false
            }
        }
    }

    private val _taskUpdated = MutableLiveData<Boolean>()
    val taskUpdated: LiveData<Boolean> get() = _taskUpdated

    fun updateTask(task:TaskEntity){
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                _taskUpdated.value = true
            } catch (e: Exception) {
                _taskUpdated.value = false
            }

        }
    }

}

class AddEditTaskViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java)) {
            val repository = TaskRepository(taskDao)
            return AddEditTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}