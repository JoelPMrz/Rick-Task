package com.example.ricktasks.ui.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.ricktasks.R
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.databinding.ItemTaskBinding

class TasksAdapter(private val tasks : List<TaskEntity>): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater
            .from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }


    class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(task : TaskEntity){
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDate.text = task.date
            binding.chipFormTaskCompleted.isChecked = task.isCompleted
        }
    }
}