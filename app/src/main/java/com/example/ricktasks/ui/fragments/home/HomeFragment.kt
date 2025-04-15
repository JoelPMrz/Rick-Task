package com.example.ricktasks.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ricktasks.MainActivity
import com.example.ricktasks.R
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.database.AppDatabase
import com.example.ricktasks.databinding.FragmentHomeBinding
import com.example.ricktasks.ui.adapters.TasksAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var taskDao: TaskDao

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

        homeViewModel.getAllTasks()

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
    }
}