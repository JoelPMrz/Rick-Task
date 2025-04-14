package com.example.ricktasks.ui.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.databinding.ItemTaskBinding

class TasksAdapter(
    private val onTaskDelete: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private var tasks: List<TaskEntity> = emptyList()

    fun submitList(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], onTaskDelete,)
    }

    class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity, onTaskDelete: (TaskEntity) -> Unit) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDate.text = task.date
            binding.chipFormTaskCompleted.isChecked = task.isCompleted
            binding.deleteIconTask.setOnClickListener{
                onTaskDelete(task)
            }
            binding.root.setOnClickListener{

            }
        }
    }
}
