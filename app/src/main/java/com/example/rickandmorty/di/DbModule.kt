package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.data.data_sources.local.PersonsDao
import com.example.rickandmorty.data.data_sources.local.RickAndMortyDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    private companion object {
        const val DB_NAME = "rickAndMortyDb"
    }

    @Provides
    @Singleton
    fun provideDb(context: Context): RickAndMortyDb =
        Room.databaseBuilder(context, RickAndMortyDb::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun providePersonsDao(db: RickAndMortyDb): PersonsDao = db.getPersonsDao()
}