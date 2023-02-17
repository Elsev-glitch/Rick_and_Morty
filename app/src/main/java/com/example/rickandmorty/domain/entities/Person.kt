package com.example.rickandmorty.domain.entities

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.*

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
) {

    fun isAlive() = status?.equals("alive", true) == true

    fun createdDateWithoutTime(): String? = createdDateTimestamp()?.let {
        SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU")).format(Date(it))
    }

    fun createdTime(): String? = createdDateTimestamp()?.let {
        SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(Date(it))
    }

    private fun createdDateTimestamp() = try {
        DateTime.parse(created).withZone(DateTimeZone.forOffsetHours(3)).millis
    } catch (ignored: Exception) {
        null
    }
}

data class Origin(
    val name: String?,
    val url: String?
)

data class Location(
    val name: String?,
    val url: String?
)