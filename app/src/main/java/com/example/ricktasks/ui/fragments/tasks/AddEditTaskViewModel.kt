package com.example.ricktasks.ui.fragments.tasks

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

    fun getTaskById(id:Int){
        viewModelScope.launch {
            _task.value = repository.getTaskById(id)
        }
    }

    private val _taskInserted = MutableLiveData<Boolean>()
    val taskInserted: LiveData<Boolean> get() = _taskInserted

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            try {
                // Intentar insertar la tarea
                repository.insertTask(task)
                _taskInserted.value = true  // La tarea fue insertada correctamente
            } catch (e: Exception) {
                _taskInserted.value = false  // Hubo un error al insertar la tarea
            }
        }
    }

    fun updateTask(task:TaskEntity){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    private var _tasks = MutableLiveData<List<TaskEntity>>()
    val tasks: LiveData<List<TaskEntity>> get() = _tasks

    fun getAllTasks(){
        viewModelScope.launch {
            _tasks.value = repository.getAllTasks()
        }
    }

}

class AddEditTaskViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java)) {
            // Crear TaskRepository pasando el taskDao
            val repository = TaskRepository(taskDao)
            return AddEditTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}