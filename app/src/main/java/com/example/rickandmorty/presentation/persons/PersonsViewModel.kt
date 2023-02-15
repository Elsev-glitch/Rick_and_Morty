package com.example.rickandmorty.presentation.persons

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.core.mvvm.BaseViewModel
import com.example.rickandmorty.domain.usecases.paging.GetPersonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    private val getPersonsUseCase: GetPersonsUseCase
): BaseViewModel() {

    val personsPagingData = flow {
        emitAll(getPersonsUseCase(GetPersonsUseCase.Params()))
    }.cachedIn(viewModelScope)
}