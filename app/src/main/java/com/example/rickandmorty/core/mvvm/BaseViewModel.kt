package com.example.rickandmorty.core.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.core.error.Failure
import com.example.rickandmorty.core.usecases.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

open class BaseViewModel : ViewModel() {

    private val loadingSemaphore = AtomicInteger(0)

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    private val mFailure: MutableLiveData<Event<Failure>> = MutableLiveData()
    val failure: LiveData<Event<Failure>>
        get() = mFailure

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private fun decrementFailure(failure: Failure) {
        when (failure) {
            is Failure.Canceled -> Timber.d("canceled")
            else -> mFailure.value = Event(failure)
        }
        decrementLoading()
    }

    fun resetLoading() {
        loadingSemaphore.set(0)
    }

    private fun incrementLoading() {
        val current = loadingSemaphore.getAndIncrement()
        if (current == 0) {
            mLoading.value = true
        }
    }

    private fun decrementLoading() {
        val current = loadingSemaphore.get()
        Timber.d("alarm1: $current")
        if (current > 0) {
            val result = loadingSemaphore.decrementAndGet()
            if (result == 0) {
                mLoading.value = false
            }
        }
    }

    protected suspend fun <T> handleMultiple(
        resource: Resource<T>,
        doOnSuccess: suspend (Resource.Success<T>) -> Unit = {}
    ) {
        when (resource) {
            is Resource.Loading -> incrementLoading()
            is Resource.Error -> {
                decrementFailure(resource.failure)
            }
            is Resource.Success -> {
                decrementLoading()
                doOnSuccess(resource)
            }
        }
    }

    protected fun launch(func: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { func() }
    }
}