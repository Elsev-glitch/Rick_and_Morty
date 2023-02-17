package com.example.rickandmorty.presentation.persons

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.core.mvvm.BaseViewModel
import com.example.rickandmorty.domain.usecases.GetPersonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    private val getPersonsUseCase: GetPersonsUseCase
) : BaseViewModel() {

    private val mSearchedPersonName = MutableStateFlow<String?>(null)
    val searchedPersonName = mSearchedPersonName.asStateFlow()

    val personsPagingData = mSearchedPersonName.flatMapLatest {
        getPersonsUseCase.execute(GetPersonsUseCase.Params(it))
    }.cachedIn(viewModelScope)

    fun submitPersonName(name: String?) {
        mSearchedPersonName.value = name
    }
}