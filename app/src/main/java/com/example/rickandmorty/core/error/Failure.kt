package com.example.rickandmorty.core.error

sealed class Failure {

    abstract fun message(): String

    data class ServerError(private val error: String) : Failure() {
        override fun message() = error
    }

    object Canceled : Failure() {
        override fun message(): String = ""
    }

    companion object {
        fun handle(exception: Exception): Failure {
            return ServerError("Неизвестная ошибка")
        }
    }
}