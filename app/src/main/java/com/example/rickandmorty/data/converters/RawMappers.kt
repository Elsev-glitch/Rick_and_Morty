package com.example.rickandmorty.data.converters

import com.example.rickandmorty.data.models.RawLocation
import com.example.rickandmorty.data.models.RawOrigin
import com.example.rickandmorty.data.models.RawPerson
import com.example.rickandmorty.domain.entities.Location
import com.example.rickandmorty.domain.entities.Origin
import com.example.rickandmorty.domain.entities.Person

fun RawPerson.asPerson() = Person(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin?.asOrigin(),
    location = location?.asLocation(),
    image = image,
    episode = episode,
    url = url,
    created = created
)

fun RawOrigin.asOrigin() = Origin(
    name = name,
    url = url
)

fun RawLocation.asLocation() = Location(
    name = name,
    url = url
)