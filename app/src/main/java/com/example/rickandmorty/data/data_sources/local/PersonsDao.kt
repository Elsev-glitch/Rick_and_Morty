package com.example.rickandmorty.data.data_sources.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.rickandmorty.data.models.RawPerson

@Dao
interface PersonsDao {

    @Query("SELECT * FROM persons")
    fun getPagingSource(): PagingSource<Int, RawPerson>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(persons: List<RawPerson>?)

    @Query("DELETE FROM persons")
    suspend fun clear()

    @Transaction
    suspend fun refresh(persons: List<RawPerson>?) {
        clear()
        save(persons)
    }

    suspend fun save(person: RawPerson) {
        save(listOf(person))
    }
}