package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yessorae.data.Constants.COL_DATE_FROM
import com.yessorae.data.Constants.COL_GOAL_ID
import com.yessorae.data.Constants.COL_UPPER_GOAL_ID
import com.yessorae.data.Constants.TABLE_GOAL
import com.yessorae.data.Constants.TABLE_TODO
import com.yessorae.data.local.database.model.GoalEntity
import com.yessorae.data.local.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface GoalDao : BaseDao<GoalEntity> {
    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_DATE_FROM BETWEEN :start AND :end AND type = :goalType
        """
    )
    fun loadGoalsFlow(
        start: LocalDateTime,
        end: LocalDateTime,
        goalType: String
    ): Flow<List<GoalEntity>>

    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_GOAL_ID = :id 
        """
    )
    suspend fun loadGoalById(id: Int): GoalEntity

    @Query(
        value = """
            SELECT * FROM $TABLE_TODO WHERE $COL_UPPER_GOAL_ID = :goalId
        """
    )
    suspend fun loadTodosByUpperGoalId(goalId: Int): List<TodoEntity>

    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_UPPER_GOAL_ID = :goalId
        """
    )
    suspend fun loadGoalsByUpperGoalId(goalId: Int): List<GoalEntity>

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    // todo async 고려
    @Transaction
    suspend fun deleteGoalTransaction(goal: GoalEntity) {
        val goalId = goal.goalId

        val todoEntities = loadTodosByUpperGoalId(goalId)
        val goalEntities = loadGoalsByUpperGoalId(goalId)
        todoEntities.forEach { todoEntity ->
            updateTodo(
                todoEntity.copy(
                    upperGoalId = null,
                    upperGoalContributionScore = null
                )
            )
        }
        goalEntities.forEach { goalEntity ->
            update(
                goalEntity.copy(
                    upperGoalId = null,
                    upperGoalContributionScore = null
                )
            )
        }

        processUpperGoal(goal)

        delete(goal)
    }

    // todo need test
    @Transaction
    suspend fun processUpperGoal(goal: GoalEntity) {
        goal.upperGoalId?.let { upperGoalId ->
            val upperGoal = loadGoalById(upperGoalId)
            if (goal.done) {
                // 이 때 상위목표가 미달성되었다가 달성되는 경우는 없음
                val newUpperCurrentScore =
                    upperGoal.currentScore - (goal.upperGoalContributionScore ?: 0)

                update(
                    upperGoal.copy(
                        currentScore = newUpperCurrentScore
                    )
                )

                // 달성되었다가 미달성된 경우
                if (upperGoal.done && upperGoal.totalScore > newUpperCurrentScore) {
                    processUpperGoal(upperGoal)
                }
            }
        }
    }
}