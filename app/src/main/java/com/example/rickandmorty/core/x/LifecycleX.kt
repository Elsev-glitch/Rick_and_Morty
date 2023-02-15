package com.example.rickandmorty.core.x

import androidx.lifecycle.*
import com.example.rickandmorty.core.mvvm.Event
import com.example.rickandmorty.core.mvvm.EventObserver
import kotlinx.coroutines.launch

fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.failure(liveData: L, body: (T) -> Unit) {
    val owner = this
    this.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            liveData.observe(owner, EventObserver(body))
        }
    }
}