package com.example.rickandmorty.data.data_sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.data.models.RawPerson

@Database(entities = [RawPerson::class], version = 1)
@TypeConverters(DbConverter::class)
abstract class RickAndMortyDb: RoomDatabase() {

    abstract fun getPersonsDao(): PersonsDao
}