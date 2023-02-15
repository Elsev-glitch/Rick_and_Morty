package com.example.rickandmorty.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Persons(
    val persons: List<Person>?,
    val maxPage: Int?
)

@Parcelize
data class Person(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: Origin?,
    val location: Location?,
    val image: String?,
    val episode: List<String>?,
    val url: String?,
    val created: String?
): Parcelable {

    fun isAlive() = status?.equals("alive", true) == true
}

@Parcelize
data class Origin(
    val name: String?,
    val url: String?
): Parcelable

@Parcelize
data class Location(
    val name: String?,
    val url: String?
): Parcelable