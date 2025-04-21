package com.example.ricktasks.data.repository

import com.example.ricktasks.core.base.CharactersApi
import com.example.ricktasks.data.remote.model.Character

class CharactersRepository(
    private val charactersApi: CharactersApi
) {
    suspend fun getCharacters(): List<Character> {
        val allCharacters = mutableListOf<Character>()
        var page = 1
        var keepGoing = true

        while (keepGoing) {
            val response = charactersApi.getCharacters(page)
            allCharacters.addAll(response.results)
            keepGoing = response.info.next != null
            page++
        }

        return allCharacters
    }
}