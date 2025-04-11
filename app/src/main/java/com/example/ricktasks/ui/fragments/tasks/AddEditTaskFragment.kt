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

    private lateinit var viewModel: AddEditTaskViewModel
    private lateinit var taskDao: TaskDao

    private lateinit var chip: Chip
    private var chipChecked = false
    private var titleTask: String = ""
    private var descriptionTask: String =""
    private var dateTask: String = ""
    private var isCompleted: Boolean = false
    private var newTask = TaskEntity(
        title = titleTask,
        description = descriptionTask,
        date =  dateTask,
        isCompleted = isCompleted
    )

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
            addTask()
        }

        binding.tbAddEditTask.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
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



    private fun addTask() {
        // Obtener los valores de los campos de entrada
        titleTask = binding.etTaskFormTitle.text.toString()
        descriptionTask = binding.etTaskFormDescription.text.toString()
        dateTask = binding.etTaskFormDate.text.toString()
        isCompleted = chipChecked

        // Verificar que los campos no estén vacíos
        if (titleTask.isEmpty() || descriptionTask.isEmpty() || dateTask.isEmpty()) {
            Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return // No proceder si falta algún campo
        }

        // Crear la nueva tarea con los valores obtenidos
        newTask = TaskEntity(
            title = titleTask,
            description = descriptionTask,
            date = dateTask,
            isCompleted = isCompleted
        )

        // Insertar la tarea en la base de datos
        viewModel.insertTask(newTask)

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