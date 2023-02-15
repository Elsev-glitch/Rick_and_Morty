package com.example.rickandmorty.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.rickandmorty.core.image.GlideImageLoader
import com.example.rickandmorty.core.image.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideImageHelper(glide: GlideImageLoader): ImageLoader

    companion object {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context = context

        @Provides
        @Singleton
        fun provideRequestManager(context: Context): RequestManager = Glide.with(context)
    }
}