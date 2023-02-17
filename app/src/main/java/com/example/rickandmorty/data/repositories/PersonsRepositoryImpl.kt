package com.example.rickandmorty.data.repositories

import androidx.paging.*
import com.example.rickandmorty.data.data_sources.local.PersonsDao
import com.example.rickandmorty.data.data_sources.remote.Api
import com.example.rickandmorty.data.model_mappers.asPerson
import com.example.rickandmorty.data.remote_mediators.PersonsRemoteMediator
import com.example.rickandmorty.domain.entities.Person
import com.example.rickandmorty.domain.repositories.PersonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalPagingApi
class PersonsRepositoryImpl constructor(
    private val api: Api,
    private val personsRemoteMediatorFactory: PersonsRemoteMediator.Factory,
    private val personsDao: PersonsDao
) : PersonsRepository {

    private companion object {
        const val PAGE_SIZE = 20
    }

    override suspend fun getPerson(personId: Int): Person {
        return api.getPerson(personId).asPerson()
    }

    override suspend fun getPersons(queryName: String?): Flow<PagingData<Person>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = personsRemoteMediatorFactory.create(queryName),
            pagingSourceFactory = { personsDao.getPagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map {
                    it.asPerson()
                }
            }
    }
}