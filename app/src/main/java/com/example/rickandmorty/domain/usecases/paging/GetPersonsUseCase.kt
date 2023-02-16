package com.example.rickandmorty.domain.usecases.paging

import androidx.paging.PagingSource
import com.example.rickandmorty.core.usecases.paging.PagingUseCase
import com.example.rickandmorty.domain.entities.Person
import com.example.rickandmorty.domain.repositories.PersonsRepository
import javax.inject.Inject

class GetPersonsUseCase @Inject constructor(
    private val repository: PersonsRepository
): PagingUseCase<Int, Person, GetPersonsUseCase.Params>() {

    override fun loadSize(): Int = 20

    override fun source(params: Params): PagingSource<Int, Person> {
        return PersonsSource(repository = repository, requestParams = params)
    }

    class Params(
        val name: String?
    )
}