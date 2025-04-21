package com.example.ricktasks.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ricktasks.R
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.databinding.ItemTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TasksAdapter(
    private val onTaskClick: (TaskEntity) -> Unit,
    private val onTaskDelete: (TaskEntity) -> Unit,
    private val onTaskState: (TaskEntity) -> Unit
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
        holder.bind(tasks[position], onTaskClick, onTaskDelete, onTaskState)
    }

    class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: TaskEntity,
            onTaskClick: (TaskEntity) -> Unit,
            onTaskDelete: (TaskEntity) -> Unit,
            onTaskState : (TaskEntity) -> Unit
        ) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDate.text = task.date
            binding.chipFormTaskCompleted.isChecked = task.isCompleted

            chipUpdateColors(task.isCompleted)

            binding.root.setOnClickListener {
                onTaskClick(task)
            }

            binding.deleteIconTask.setOnClickListener {
                val dialogView =
                    LayoutInflater.from(binding.root.context).inflate(R.layout.delete_task_dialog, null)

                val dialog = MaterialAlertDialogBuilder(binding.root.context)
                    .setView(dialogView)
                    .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton(R.string.delete) { dialogInterface, _ ->
                        onTaskDelete(task)
                        dialogInterface.dismiss()
                    }
                    .show()
                dialog.setCancelable(true)
            }

            binding.chipFormTaskCompleted.setOnClickListener{
                onTaskState(task)
            }

        }

        private fun chipUpdateColors(isCompleted: Boolean) {

            val chip = binding.chipFormTaskCompleted

            if (isCompleted) {
                chip.setChipBackgroundColorResource(R.color.md_theme_secondaryContainer)
                chip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.md_theme_onSecondaryContainer))
                chip.setChipIconResource(R.drawable.ic_check_circle_24dp)
                chip.setChipIconTintResource(R.color.md_theme_onSecondaryContainer)
            } else {
                chip.setChipBackgroundColorResource(R.color.md_theme_errorContainer)
                chip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.md_theme_onErrorContainer))
                chip.setChipIconResource(R.drawable.ic_cancel_circle_24dp)
                chip.setChipIconTintResource(R.color.md_theme_onErrorContainer)
            }
        }


    }
}

