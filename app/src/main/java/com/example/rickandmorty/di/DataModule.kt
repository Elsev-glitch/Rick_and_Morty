package com.example.rickandmorty.di

import com.example.rickandmorty.data.data_sources.remote.Api
import com.example.rickandmorty.data.repositories.PersonsRepositoryImpl
import com.example.rickandmorty.domain.repositories.PersonsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providePersonsRepository(api: Api): PersonsRepository = PersonsRepositoryImpl(api)
}