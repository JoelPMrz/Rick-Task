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
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.databinding.FragmentHomeBinding
import com.example.ricktasks.ui.adapters.TasksAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).showBottomNav()
        binding.rvTasks.adapter = TasksAdapter(
            listOf(
                TaskEntity(1,"Titulo 1", "Esto es una descripcion 1", "22 Marzo 2025", false),
                TaskEntity(2,"Titulo 2 algo más larguete ", "Esto es una descripcion 2", "05 Enero 2025", false),
                TaskEntity(3,"Titulo 3", "Esto es una descripcion 3", "30 Noviembre 2024", true)
            )
        )
        listeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun listeners(){
        binding.fabAddTask.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_navigation_add_edit_task)
        }
    }
}