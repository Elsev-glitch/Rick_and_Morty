package com.example.rickandmorty.data.data_sources.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class DbConverter {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun fromListString(data: List<String>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toListString(data: String?): List<String>? {
        return data?.let {
            gson.fromJson(it, object : TypeToken<List<String>>(){}.type)
        }
    }
}