package com.foresight.app.data.local.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromLong(value: Long?): String = value?.toString() ?: ""

    @TypeConverter
    fun toLong(value: String): Long? = value.toLongOrNull()

    @TypeConverter
    fun fromBoolean(value: Boolean): Int = if (value) 1 else 0

    @TypeConverter
    fun toBoolean(value: Int): Boolean = value == 1
}
