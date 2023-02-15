package com.example.rickandmorty.domain.usecases.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.domain.entities.Person
import com.example.rickandmorty.domain.repositories.PersonsRepository
import retrofit2.HttpException
import java.io.IOException

class PersonsSource(
    private val repository: PersonsRepository,
    private val requestParams: GetPersonsUseCase.Params
) : PagingSource<Int, Person>() {

    companion object {
        private const val START_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return try {
            val page = params.key ?: START_PAGE
            val response = repository.getPersons(
                page = page,
                name = requestParams.name
            )
            LoadResult.Page(
                data = response?.persons.orEmpty(),
                prevKey = if (page == START_PAGE) null else page - START_PAGE,
                nextKey = if (response?.persons.isNullOrEmpty() || response?.maxPage?.let { it <= page } == true) null else page + START_PAGE
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}