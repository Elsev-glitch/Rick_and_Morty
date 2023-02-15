package com.example.rickandmorty.core.usecases.paging

import androidx.paging.*
import kotlinx.coroutines.flow.Flow

abstract class PagingUseCase<Key : Any, Type : Any, in Params> {

    private val factories = mutableListOf<InvalidatingPagingSourceFactory<Key, Type>>()

    open suspend operator fun invoke(params: Params): Flow<PagingData<Type>> {
        val factory = InvalidatingPagingSourceFactory { source(params) }
        factories.add(factory)
        return Pager(
            config = PagingConfig(
                pageSize = loadSize(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = factory
        ).flow
    }

    abstract fun loadSize(): Int
    abstract fun source(params: Params): PagingSource<Key, Type>
    open fun map(result: PagingData<Type>): PagingData<Type> {
        return result
    }

    fun invalidate() {
        for (factory in factories) {
            factory.invalidate()
        }
    }
}