package com.example.ricktasks.ui.fragments.characters

import androidx.lifecycle.*
import com.example.ricktasks.data.remote.model.Character
import com.example.ricktasks.data.repository.CharactersRepository
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>(emptyList())
    val characters: LiveData<List<Character>> get() = _characters

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var isLastPage = false

    init {
        loadCharacters()
    }

    fun loadCharacters() {

        if (_isLoading.value == true || isLastPage) return

        _isLoading.value = true

        viewModelScope.launch {
            try {

                val response = charactersRepository.getCharacters()
                val currentList = _characters.value ?: emptyList()
                _characters.value = currentList + response

                isLastPage = response.isEmpty()
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}



class CharactersViewModelFactory(
    private val repository: CharactersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            return CharactersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
