package com.example.ricktasks.core.base

import com.example.ricktasks.data.remote.model.CharacterResponse
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponse

}