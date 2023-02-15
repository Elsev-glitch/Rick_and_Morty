package com.example.rickandmorty.presentation.person_description

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.core.ARG_PERSON_ID
import com.example.rickandmorty.core.mvvm.BaseViewModel
import com.example.rickandmorty.domain.usecases.GetPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PersonDescriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPersonUseCase: GetPersonUseCase
) : BaseViewModel() {

    private val mPersonId = MutableStateFlow<Int?>(savedStateHandle[ARG_PERSON_ID])

    val person = mPersonId.filterNotNull().flatMapLatest {
        getPersonUseCase(GetPersonUseCase.Params(it))
    }
        .onEach { handleMultiple(it) }
        .mapNotNull { it.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}