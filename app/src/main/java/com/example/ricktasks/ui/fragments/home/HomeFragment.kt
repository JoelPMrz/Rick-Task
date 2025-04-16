package com.example.ricktasks.ui.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ricktasks.MainActivity
import com.example.ricktasks.R
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.database.AppDatabase
import com.example.ricktasks.databinding.FragmentHomeBinding
import com.example.ricktasks.ui.adapters.TasksAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var taskDao: TaskDao

    private var showDialog : Boolean = false
    private var filter  = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setUp()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUp(){
        (activity as MainActivity).showBottomNav()

        taskDao = AppDatabase.getDatabase(requireContext()).taskDao()
        val factory = HomeViewModelFactory(taskDao)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        tasksAdapter = TasksAdapter(
            onTaskClick = { task ->
                val action = HomeFragmentDirections
                    .actionNavigationHomeToNavigationAddEditTask(task = task)
                findNavController().navigate(action)
            },

            onTaskDelete = { task ->
                homeViewModel.deleteTask(task)
                homeViewModel.getAllTasks()
            },

            onTaskState={ task->
                task.isCompleted = !task.isCompleted
                homeViewModel.updateTask(task)
                homeViewModel.getAllTasks()
            }
        )
        binding.rvTasks.adapter = tasksAdapter

        getFilterTasksList()

        homeViewModel.tasks.observe(viewLifecycleOwner) { taskList ->
            tasksAdapter.submitList(taskList)
        }
        listeners()
    }

    private fun listeners(){
        binding.fabAddTask.setOnClickListener{
            val action = HomeFragmentDirections
                .actionNavigationHomeToNavigationAddEditTask()
            findNavController().navigate(action)
        }

        binding.icFilterList.setOnClickListener{
            showDialog = true
            filterTasksDialog()
        }




    }

    private fun filterTasksDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.filter_tasks_dialog, null)
        val notCompletedTextView = dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_not_completed)
        val completedTextView = dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_completed)
        val allTextView = dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_all)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        // Cierre al tocar fuera
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnCancelListener {
            showDialog = false
        }

        notCompletedTextView.setOnClickListener {
            filter = 0
            getFilterTasksList()
            dialog.dismiss()
        }

        completedTextView.setOnClickListener {
            filter = 1
            getFilterTasksList()
            dialog.dismiss()
        }

        allTextView.setOnClickListener {
            filter = 2
            getFilterTasksList()
            dialog.dismiss()
        }

        if (showDialog) dialog.show()
    }

    private fun getFilterTasksList(){
        when(filter){
            0 -> homeViewModel.getNotCompletedTasks()
            1-> homeViewModel.getCompletedTasks()
            else -> homeViewModel.getAllTasks()
        }
    }
}