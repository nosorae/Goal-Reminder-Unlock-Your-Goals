package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yessorae.data.DataConstants.COL_DATE_FROM
import com.yessorae.data.DataConstants.COL_GOAL_ID
import com.yessorae.data.DataConstants.COL_UPPER_GOAL_ID
import com.yessorae.data.DataConstants.TABLE_GOAL
import com.yessorae.data.DataConstants.TABLE_TODO
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
    suspend fun updateGoalTransaction(new: GoalEntity) {
        val old = loadGoalById(new.goalId)

        // 하위 목표 먼저 업데이트 후
        update(new)

        // 예전 상위 목표 업데이트
        if (new.upperGoalId != old.upperGoalId) {
            old.upperGoalId?.let { oldUpperGoalId ->
                updateUpperGoalScoreTransaction(upperGoalId = oldUpperGoalId)
            }
        }

        // 새로운 상위 목표 업데이트
        new.upperGoalId?.let { newUpperGoalId ->
            updateUpperGoalScoreTransaction(upperGoalId = newUpperGoalId)
        }
    }

    // todo async 고려
    @Transaction
    suspend fun deleteGoalTransaction(deletedGoal: GoalEntity) {
        val goalId = deletedGoal.goalId

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

        // 상위 목표 업데이트
        deletedGoal.upperGoalId?.let { upperGoalId ->
            updateUpperGoalScoreTransaction(upperGoalId = upperGoalId)
        }

        delete(deletedGoal)
    }

    // todo async 고려
    @Transaction
    suspend fun updateUpperGoalScoreTransaction(upperGoalId: Int) {
        val upperGoal = loadGoalById(upperGoalId)

        val contributionTodos = loadTodosByUpperGoalId(upperGoalId)
        val contributionGoals = loadGoalsByUpperGoalId(upperGoalId)
        val todosScore = contributionTodos.sumOf {
            if (it.done) {
                it.upperGoalContributionScore ?: 0
            } else {
                0
            }
        }
        val goalsScore = contributionGoals.sumOf {
            if (it.done) {
                it.upperGoalContributionScore ?: 0
            } else {
                0
            }
        }

        // 하위 먼저 업데이트 후
        update(
            upperGoal.copy(
                currentScore = todosScore + goalsScore
            )
        )

        // 상위 업데이트
        upperGoal.upperGoalId?.let {
            updateUpperGoalScoreTransaction(upperGoalId = it)
        }
    }
}
