package com.example.ricktasks.ui.fragments.tasks

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ricktasks.MainActivity
import com.example.ricktasks.R
import com.example.ricktasks.data.local.dao.TaskDao
import com.example.ricktasks.data.local.database.AppDatabase
import com.example.ricktasks.data.local.entity.TaskEntity
import com.example.ricktasks.data.repository.TaskRepository
import com.example.ricktasks.databinding.FragmentAddEditTaskBinding
import com.google.android.material.chip.Chip
import java.time.LocalDate

class AddEditTaskFragment: Fragment() {

    private var _binding : FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AddEditTaskFragmentArgs>()

    private lateinit var viewModel: AddEditTaskViewModel
    private lateinit var taskDao: TaskDao

    private lateinit var chip: Chip
    private var chipChecked = false
    private var titleTask: String = ""
    private var descriptionTask: String =""
    private var dateTask: String = ""
    private var isCompleted: Boolean = false
    var task : TaskEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        (activity as MainActivity).hideBottomNav()

        taskDao = AppDatabase.getDatabase(requireContext()).taskDao()
        val factory = AddEditTaskViewModelFactory(taskDao)
        viewModel = ViewModelProvider(this, factory).get(AddEditTaskViewModel::class.java)

        chip = binding.chipFormTaskCompleted

        handleEditMode()

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Configuraciones previas
    private fun setUp(){
        listeners()
        chip.setChecked(chipChecked)
        chipUpdateColors()
    }

    //Llamada para los listeners, controlar los clicks
    private fun listeners(){
        binding.tbAddEditTask.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        chip.setOnClickListener {
            chipChecked = !chipChecked
            chip.setChecked(chipChecked)
            chipUpdateColors()
        }

        binding.tilTaskFormDate.setEndIconOnClickListener {
            showDatePickerDialog()
        }

        binding.btnAddEditTask.setOnClickListener{
            if(args.task != null){
                updateTask()
            }else{
                addTask()
            }
        }

        binding.tbAddEditTask.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun handleEditMode(){
        if(args.task != null){
            task = args.task!!

            titleTask = task!!.title
            descriptionTask = task!!.description
            dateTask = task!!.date
            chipChecked = task!!.isCompleted

            binding.tvAddEditTaskTitle.text = "Editar tarea"
            binding.etTaskFormTitle.setText(titleTask)
            binding.etTaskFormDescription.setText(descriptionTask)
            binding.etTaskFormDate.setText(dateTask)
            binding.chipFormTaskCompleted.isChecked = chipChecked
            chipUpdateColors()

            binding.btnAddEditTask.text = "Editar"
        }
    }

    private fun updateTask(){
        val taskToUpdate = updateValues() ?: return
        viewModel.updateTask(taskToUpdate)

        viewModel.taskUpdated.observe(viewLifecycleOwner, Observer { isUpdated ->
            if (isUpdated) {
                Toast.makeText(context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Error al editar la tarea", Toast.LENGTH_SHORT).show()
            }
        })
    }


    //Actualizar colores e iconos
    private fun chipUpdateColors(){
        if(chip.isChecked){
            chip.setChipBackgroundColorResource(R.color.md_theme_secondaryContainer)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onSecondaryContainer))
            chip.setChipIconResource(R.drawable.ic_check_circle_24dp)
            chip.setChipIconTintResource(R.color.md_theme_onSecondaryContainer)
        }else{
            chip.setChipBackgroundColorResource(R.color.md_theme_errorContainer)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onErrorContainer))
            chip.setChipIconResource(R.drawable.ic_cancel_circle_24dp)
            chip.setChipIconTintResource(R.color.md_theme_onErrorContainer)
        }
    }

    private fun updateValues(): TaskEntity? {
        titleTask = binding.etTaskFormTitle.text.toString()
        descriptionTask = binding.etTaskFormDescription.text.toString()
        dateTask = binding.etTaskFormDate.text.toString()
        isCompleted = chipChecked

        if (titleTask.isEmpty() || descriptionTask.isEmpty() || dateTask.isEmpty()) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return null
        }

        return TaskEntity(
            id = task?.id ?: 0,
            title = titleTask,
            description = descriptionTask,
            date = dateTask,
            isCompleted = isCompleted
        )
    }


    private fun addTask() {
        val taskToInsert = updateValues() ?: return
        viewModel.insertTask(taskToInsert)

        // Observar si la tarea se insertó correctamente
        viewModel.taskInserted.observe(viewLifecycleOwner, Observer { isInserted ->
            if (isInserted) {
                // La tarea se insertó correctamente
                Toast.makeText(context, "Tarea creada con éxito", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Volver al fragmento anterior
            } else {
                // Hubo un error al crear la tarea
                Toast.makeText(context, "Error al crear la tarea", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etTaskFormDate.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }






}