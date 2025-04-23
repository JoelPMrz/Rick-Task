package com.example.ricktasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ricktasks.MainActivity
import com.example.ricktasks.R
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.database.AppDatabase
import com.example.ricktasks.data.repository.PreferencesRepository
import com.example.ricktasks.databinding.FragmentHomeBinding
import com.example.ricktasks.di.PreferencesProvider
import com.example.ricktasks.ui.adapters.TasksAdapter
import com.example.ricktasks.ui.viewModels.HomeViewModel
import com.example.ricktasks.ui.viewModels.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var taskDao: TaskDao

    private lateinit var preferencesRepository: PreferencesRepository
    private var darkMode = false

    private var showFilterDialog: Boolean = false
    private var filter = 2


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

    private fun setUp() {
        (activity as MainActivity).showBottomNav()

        taskDao = AppDatabase.getDatabase(requireContext()).taskDao()
        val factory = HomeViewModelFactory(taskDao)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        preferencesRepository = PreferencesProvider.provideRepository(requireContext())
        darkMode = preferencesRepository.getThemePreference()
        binding.icDarkLightMode.setImageResource(if (darkMode) R.drawable.ic_dark_mode else R.drawable.ic_light_mode)
        filter = preferencesRepository.getTaskFilter()

        tasksAdapter = TasksAdapter(
            onTaskClick = { task ->
                val action = HomeFragmentDirections
                    .actionNavigationHomeToNavigationAddEditTask(task = task)
                findNavController().navigate(action)
            },

            onTaskDelete = { task ->
                homeViewModel.deleteTask(task)
                getFilterTasksList()
                Toast.makeText(binding.root.context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
            },

            onTaskState = { task ->
                task.isCompleted = !task.isCompleted
                homeViewModel.updateTask(task)
                getFilterTasksList()
            }
        )
        binding.rvTasks.adapter = tasksAdapter

        getFilterTasksList()

        homeViewModel.tasks.observe(viewLifecycleOwner) { taskList ->
            tasksAdapter.submitList(taskList)
            if (taskList.isEmpty()) {
                binding.clNoTasks.visibility = View.VISIBLE
                binding.rvTasks.visibility = View.GONE
            } else {
                binding.clNoTasks.visibility = View.GONE
                binding.rvTasks.visibility = View.VISIBLE
            }
        }
        listeners()
    }

    private fun listeners() {
        binding.fabAddTask.setOnClickListener {
            val action = HomeFragmentDirections
                .actionNavigationHomeToNavigationAddEditTask()
            findNavController().navigate(action)
        }

        binding.icFilterList.setOnClickListener {
            showFilterDialog = true
            filterTasksDialog()
        }

        binding.icDarkLightMode.setOnClickListener {
            darkMode = !darkMode
            preferencesRepository.saveThemePreference(darkMode)
            requireActivity().recreate()
        }
    }



    private fun filterTasksDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.filter_tasks_dialog, null)
        val notCompletedTextView =
            dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_not_completed)
        val completedTextView =
            dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_completed)
        val allTextView = dialogView.findViewById<TextView>(R.id.filter_tasks_dialog_all)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnCancelListener {
            showFilterDialog = false
        }

        notCompletedTextView.setOnClickListener {
            filter = 0
            preferencesRepository.saveTaskFilter(filter)
            getFilterTasksList()
            dialog.dismiss()
        }

        completedTextView.setOnClickListener {
            filter = 1
            preferencesRepository.saveTaskFilter(filter)
            getFilterTasksList()
            dialog.dismiss()
        }

        allTextView.setOnClickListener {
            filter = 2
            preferencesRepository.saveTaskFilter(filter)
            getFilterTasksList()
            dialog.dismiss()
        }

        if (showFilterDialog) dialog.show()
    }

    private fun getFilterTasksList() {
        when (filter) {
            0 -> {
                homeViewModel.getNotCompletedTasks()
                binding.tvFilterSubtitle.text = getString(R.string.not_completed)
                binding.tvFilterSubtitle.visibility = View.VISIBLE
            }
            1 -> {
                homeViewModel.getCompletedTasks()
                binding.tvFilterSubtitle.text = getString(R.string.completed)
                binding.tvFilterSubtitle.visibility = View.VISIBLE
            }
            else -> {
                homeViewModel.getAllTasks()
                binding.tvFilterSubtitle.visibility = View.GONE
            }
        }
    }
}