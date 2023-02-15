package com.example.rickandmorty.core.usecases.paging

data class BasePagingResponse<T>(
    val info: PagingResponseInfo?,
    val results: List<T>?
)

data class PagingResponseInfo(
    val count: Int?,
    val pages: Int?,
    val next: String?,
    val prev: String?
)