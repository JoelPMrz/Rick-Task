package com.example.ricktasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ricktasks.R
import com.example.ricktasks.core.network.RetrofitInstance
import com.example.ricktasks.core.utils.isNetworkAvailable
import com.example.ricktasks.core.utils.showNoConnectionDialog
import com.example.ricktasks.data.repository.CharactersRepository
import com.example.ricktasks.databinding.FragmentCharactersBinding
import com.example.ricktasks.ui.adapters.CharactersAdapter
import com.example.ricktasks.ui.viewModels.CharactersViewModel
import com.example.ricktasks.ui.viewModels.CharactersViewModelFactory
import com.example.ricktasks.utils.SharedPreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var charactersAdapter: CharactersAdapter
    private lateinit var viewModel: CharactersViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireContext().getSharedPreferences("rick_preferences", android.content.Context.MODE_PRIVATE)
        val preferencesManager = SharedPreferencesManager(sharedPrefs)
        val preferencesRepository = com.example.ricktasks.data.repository.PreferencesRepository(preferencesManager)

        val charactersApi = RetrofitInstance.api
        val repository = CharactersRepository(charactersApi)

        val factory = CharactersViewModelFactory(repository, preferencesRepository)
        viewModel = ViewModelProvider(this, factory)[CharactersViewModel::class.java]

        charactersAdapter = CharactersAdapter(preferencesRepository)

        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = charactersAdapter
        }

        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            charactersAdapter.submitList(characters)
        }

        binding.rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && viewModel.isLoading.value == false) {
                    viewModel.loadCharacters()
                }
            }
        })

        if (!isNetworkAvailable(binding.root.context)) {
            showNoConnectionDialog(requireContext()) {
                findNavController().navigate(R.id.action_charactersFragment_self)
            }
        } else {
            viewModel.loadCharacters()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
