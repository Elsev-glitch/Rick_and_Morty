package com.example.rickandmorty.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class RawPerson(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    @Embedded("origin")
    val origin: RawOrigin?,
    @Embedded("location")
    val location: RawLocation?,
    val image: String?,
    val episode: List<String>?,
    val url: String?,
    val created: String?
)

data class RawOrigin(
    val name: String?,
    val url: String?
)

data class RawLocation(
    val name: String?,
    val url: String?
)