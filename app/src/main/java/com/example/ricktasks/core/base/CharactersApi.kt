package com.example.ricktasks.core.base

import retrofit2.http.Query
import com.example.ricktasks.data.remote.model.CharactersList
import retrofit2.http.GET

interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharactersList
}