package com.yessorae.data.local.database.converter

import androidx.room.TypeConverter
import com.yessorae.domain.model.type.GoalType

class GoalTypeConverter {
    @TypeConverter
    fun fromEnumTypeToString(goalType: GoalType?): String? {
        return goalType?.name
    }

    @TypeConverter
    fun fromStringToEnumType(goalTypeName: String?): GoalType? {
        return goalTypeName?.let { GoalType.valueOf(it) }
    }
}