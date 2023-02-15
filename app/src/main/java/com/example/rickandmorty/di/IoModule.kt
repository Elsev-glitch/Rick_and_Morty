package com.example.rickandmorty.di

import com.example.rickandmorty.BuildConfig
import com.example.rickandmorty.data.data_sources.remote.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IOModule {

    @Provides
    internal fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    internal fun providesGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    private fun getOkhttpBuilder(
        vararg interceptors: Interceptor
    ): OkHttpClient.Builder {
        val timeout = 30L
        val builder = OkHttpClient.Builder()
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .apply {
                interceptors.forEach {
                    addInterceptor(it)
                }
            }

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        return builder
    }

    @Provides
    @Singleton
    internal fun provideBaseClient(): OkHttpClient =
        getOkhttpBuilder().build()

    @Provides
    @Singleton
    internal fun provideRetrofit(
        converter: Converter.Factory,
        client: OkHttpClient,
        url: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(converter)
            .build()

    @Provides
    @Singleton
    internal fun provideApi(
        retrofit: Retrofit
    ): Api = retrofit.create(Api::class.java)
}