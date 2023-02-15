package com.example.rickandmorty.core.usecases

import com.example.rickandmorty.core.error.Failure
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class UseCase<Type, in Params> {
    abstract suspend fun run(params: Params): Flow<Resource<Type>>

    open val doOnSuccess: suspend (Type?, Params) -> Unit = { _, _ -> }
    open val doOnCompletion: () -> Unit = {}

    open suspend operator fun invoke(
        params: Params,
        doOnCancel: suspend (Resource.Error<Type>) -> Unit = {}
    ): Flow<Resource<Type>> {
        return run(params).onStart {
            Timber.d("Resource.Loading()")
            emit(Resource.Loading())
        }.onEach {
            if (it is Resource.Success) {
                doOnSuccess(it.data, params)
            }
        }.onCompletion {
            if (it is CancellationException) {
                Timber.e("Canceled")
                doOnCompletion()
                doOnCancel(Resource.Error(Failure.Canceled))
            }
        }
    }

    protected suspend fun <T> execute(
        invoke: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(
            try {
                val response = invoke()
                Resource.Success(response)
            } catch (ex: Exception) {
                Failure.handle(ex).let {
                    Resource.Error(it)
                }
            }
        )
    }.flowOn(Dispatchers.IO)
}