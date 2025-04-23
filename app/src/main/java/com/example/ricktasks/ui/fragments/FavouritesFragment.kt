package com.example.ricktasks.ui.fragments

import android.content.SharedPreferences
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
import com.example.ricktasks.data.repository.PreferencesRepository
import com.example.ricktasks.databinding.FragmentFavouritesBinding
import com.example.ricktasks.ui.adapters.CharactersAdapter
import com.example.ricktasks.ui.viewModels.CharactersViewModel
import com.example.ricktasks.ui.viewModels.CharactersViewModelFactory
import com.example.ricktasks.utils.SharedPreferencesManager

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var charactersAdapter: CharactersAdapter
    private lateinit var charactersViewModel: CharactersViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireContext().getSharedPreferences("rick_preferences", android.content.Context.MODE_PRIVATE)
        val preferencesManager = SharedPreferencesManager(sharedPrefs)
        val preferencesRepository = PreferencesRepository(preferencesManager)

        val charactersApi = RetrofitInstance.api
        val repository = CharactersRepository(charactersApi)

        val factory = CharactersViewModelFactory(repository, preferencesRepository)
        charactersViewModel = ViewModelProvider(this, factory)[CharactersViewModel::class.java]

        charactersAdapter =  CharactersAdapter(preferencesRepository)

        binding.rvFavourites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = charactersAdapter
        }

        charactersViewModel.loadFavoriteCharacters()

        // Observar la lista de personajes favoritos
        charactersViewModel.characters.observe(viewLifecycleOwner) { favoriteCharacters ->
            charactersAdapter.submitList(favoriteCharacters)  // Actualizar lista de favoritos
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}