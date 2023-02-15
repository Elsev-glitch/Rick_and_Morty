package com.example.rickandmorty.data.data_sources.remote

import com.example.rickandmorty.core.usecases.paging.BasePagingResponse
import com.example.rickandmorty.data.models.RawPerson
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("character")
    suspend fun getPersons(
        @Query("page") page: Int,
        @Query("name") name: String?
    ): BasePagingResponse<RawPerson>
}