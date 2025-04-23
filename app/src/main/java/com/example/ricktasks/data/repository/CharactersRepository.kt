package com.example.ricktasks.data.repository

import com.example.ricktasks.core.network.CharactersApi
import com.example.ricktasks.data.remote.model.CharacterResponse

class CharactersRepository(
    private val charactersApi: CharactersApi
) {

    suspend fun getCharactersPage(page: Int): CharacterResponse {
        return charactersApi.getCharacters(page)
    }

}