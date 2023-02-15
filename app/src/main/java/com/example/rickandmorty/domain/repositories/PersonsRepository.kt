package com.example.rickandmorty.domain.repositories

import com.example.rickandmorty.domain.entities.Persons

interface PersonsRepository {
    suspend fun getPersons(
        page: Int,
        name: String?
    ): Persons?
}