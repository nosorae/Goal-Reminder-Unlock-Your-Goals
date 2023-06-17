package com.yessorae.data.local.database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

class LocalDateTimeConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.toLocalDateTime()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}