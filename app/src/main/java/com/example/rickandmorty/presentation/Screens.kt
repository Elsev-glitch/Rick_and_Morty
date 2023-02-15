package com.example.rickandmorty.presentation

import com.example.rickandmorty.presentation.persons.PersonsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun persons() = FragmentScreen { PersonsFragment() }
}