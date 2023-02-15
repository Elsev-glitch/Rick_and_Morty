package com.example.rickandmorty.domain.usecases

import com.example.rickandmorty.core.usecases.Resource
import com.example.rickandmorty.core.usecases.UseCase
import com.example.rickandmorty.domain.entities.Person
import com.example.rickandmorty.domain.repositories.PersonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(
    private val repository: PersonsRepository
): UseCase<Person?, GetPersonUseCase.Params>() {

    override suspend fun run(params: Params): Flow<Resource<Person?>> =
        execute {
            repository.getPerson(params.personId)
        }

    class Params(val personId: Int)
}