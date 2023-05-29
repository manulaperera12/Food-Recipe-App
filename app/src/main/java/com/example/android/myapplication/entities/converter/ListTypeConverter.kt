package com.example.android.myapplication.entities.converter

import androidx.room.TypeConverter

// Define a TypeConverter for converting a List<String> to a String
class ListTypeConverter {

    // Convert a comma-separated String to a List<String>
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    // Convert a List<String> to a comma-separated String
    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(", ")
    }
}