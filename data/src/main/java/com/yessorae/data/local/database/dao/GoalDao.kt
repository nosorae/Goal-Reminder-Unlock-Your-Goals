package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.yessorae.data.Constants.COL_DATE_FROM
import com.yessorae.data.Constants.COL_GOAL_ID
import com.yessorae.data.Constants.TABLE_GOAL
import com.yessorae.data.local.database.model.GoalEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
abstract class GoalDao : BaseDao<GoalEntity> {
    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_DATE_FROM BETWEEN :start AND :end AND type = :goalType
        """
    )
    abstract fun loadGoalsFlow(
        start: LocalDateTime,
        end: LocalDateTime,
        goalType: String
    ): Flow<List<GoalEntity>>

    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_GOAL_ID = :id 
        """
    )
    abstract suspend fun loadGoalById(id: Int): GoalEntity
}