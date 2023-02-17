package com.example.rickandmorty.data.remote_mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.rickandmorty.data.data_sources.local.PersonsDao
import com.example.rickandmorty.data.data_sources.remote.Api
import com.example.rickandmorty.data.models.RawPerson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@ExperimentalPagingApi
class PersonsRemoteMediator @AssistedInject constructor(
    private val api: Api,
    private val dao: PersonsDao,
    @Assisted
    private val queryName: String?
): RemoteMediator<Int, RawPerson>() {

    private var page = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RawPerson>
    ): MediatorResult {

        page = getPage(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val response = api.getPersons(page, queryName)
            if (loadType == LoadType.REFRESH)
                dao.refresh(response.results)
            else
                dao.save(response.results)
            MediatorResult.Success(endOfPaginationReached = response.info?.pages?.let { page >= it } == true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPage(loadType: LoadType): Int? {
        page = when(loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++page
        }
        return page
    }

    @AssistedFactory
    interface Factory {
        fun create(queryName: String?): PersonsRemoteMediator
    }
}