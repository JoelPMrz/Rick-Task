package com.example.ricktasks.ui.viewModels

import androidx.lifecycle.*
import com.example.ricktasks.data.remote.model.Character
import com.example.ricktasks.data.repository.CharactersRepository
import com.example.ricktasks.data.repository.PreferencesRepository
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val charactersRepository: CharactersRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>(emptyList())
    val characters: LiveData<List<Character>> get() = _characters

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var isLastPage = false
    private var currentPage = 1


    fun loadCharacters() {
        if (_isLoading.value == true || isLastPage) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = charactersRepository.getCharactersPage(currentPage)
                val currentList = _characters.value ?: emptyList()
                _characters.value = currentList + response.results
                isLastPage = response.info.next == null
                currentPage++
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadFavoriteCharacters() {
        viewModelScope.launch {
            val favoriteIds = preferencesRepository.getFavorites()

            if (favoriteIds.isEmpty()) {
                _characters.value = emptyList()
                return@launch
            }

            val foundFavorites = mutableListOf<Character>()
            var page = 1
            var isLastPage = false

            while (!isLastPage && foundFavorites.size < favoriteIds.size) {
                try {
                    val response = charactersRepository.getCharactersPage(page)
                    val pageFavorites = response.results.filter { favoriteIds.contains(it.id) }

                    if (pageFavorites.isNotEmpty()) {
                        foundFavorites.addAll(pageFavorites)
                        _characters.postValue(foundFavorites.toList())
                    }

                    isLastPage = response.info.next == null
                    page++

                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }
    }

}




class CharactersViewModelFactory(
    private val repository: CharactersRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            return CharactersViewModel(repository,preferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
