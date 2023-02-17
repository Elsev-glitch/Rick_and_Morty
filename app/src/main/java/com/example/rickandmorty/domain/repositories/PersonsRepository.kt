package com.example.rickandmorty.domain.repositories

import androidx.paging.PagingData
import com.example.rickandmorty.domain.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonsRepository {

    suspend fun getPersons(queryName: String?): Flow<PagingData<Person>>

    suspend fun getPerson(personId: Int): Person?
}