package com.example.rickandmorty.domain.usecases

import androidx.paging.PagingData
import com.example.rickandmorty.domain.entities.Person
import com.example.rickandmorty.domain.repositories.PersonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonsUseCase @Inject constructor(
    private val repository: PersonsRepository
) {

    suspend fun execute(params: Params): Flow<PagingData<Person>> = repository.getPersons(params.name)

    class Params(val name: String?)
}