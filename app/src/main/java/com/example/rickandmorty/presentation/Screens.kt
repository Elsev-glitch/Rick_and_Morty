package com.example.rickandmorty.presentation

import com.example.rickandmorty.presentation.person_description.PersonDescriptionFragment
import com.example.rickandmorty.presentation.persons.PersonsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun persons() = FragmentScreen { PersonsFragment() }

    fun personDescription(personId: Int) = FragmentScreen {
        PersonDescriptionFragment.newInstance(personId)
    }
}