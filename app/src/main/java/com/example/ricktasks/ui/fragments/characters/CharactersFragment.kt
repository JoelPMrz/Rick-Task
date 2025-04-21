package com.example.ricktasks.ui.fragments.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ricktasks.core.network.RetrofitInstance
import com.example.ricktasks.data.repository.CharactersRepository
import com.example.ricktasks.databinding.FragmentCharactersBinding
import com.example.ricktasks.ui.adapters.CharactersAdapter

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

        val charactersApi = RetrofitInstance.api
        val repository = CharactersRepository(charactersApi)
        val factory = CharactersViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CharactersViewModel::class.java]

        charactersAdapter = CharactersAdapter()
        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = charactersAdapter
        }

        // Observa los personajes y actualiza el adaptador
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            charactersAdapter.submitList(characters)
        }

        // Observa el estado de carga


        // Manejo de scroll infinito
        binding.rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Verifica si el RecyclerView ha llegado al final
                if (!recyclerView.canScrollVertically(1) && !viewModel.isLoading.value!!) {
                    // Llama a loadCharacters solo si no se está cargando
                    viewModel.loadCharacters()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
