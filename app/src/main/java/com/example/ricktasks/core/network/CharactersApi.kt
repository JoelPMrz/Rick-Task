package com.example.ricktasks.core.network

import com.example.ricktasks.data.remote.model.CharacterResponse
import retrofit2.http.Query
import retrofit2.http.GET

interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponse

}