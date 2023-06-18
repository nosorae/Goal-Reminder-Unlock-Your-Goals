package com.yessorae.data.local.database.converter

import androidx.room.TypeConverter
import com.yessorae.data.local.database.model.enum.GoalType

class GoalTypeConverter {
    @TypeConverter
    fun fromGoalType(goalType: GoalType): String {
        return goalType.name
    }

    @TypeConverter
    fun toGoalType(goalTypeName: String): GoalType {
        return GoalType.valueOf(goalTypeName)
    }
}