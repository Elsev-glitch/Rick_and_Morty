package com.example.rickandmorty.data.repositories

import com.example.rickandmorty.data.converters.asPerson
import com.example.rickandmorty.data.data_sources.remote.Api
import com.example.rickandmorty.domain.entities.Persons
import com.example.rickandmorty.domain.repositories.PersonsRepository

class PersonsRepositoryImpl(
    private val api: Api
) : PersonsRepository {

    override suspend fun getPersons(
        page: Int,
        name: String?
    ): Persons {
        val response = api.getPersons(page, name)
        return Persons(
            persons = response.results?.map { it.asPerson() },
            maxPage = response.info?.pages
        )
    }
}