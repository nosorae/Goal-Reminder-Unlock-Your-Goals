package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yessorae.data.DataConstants.COL_GOAL_ID
import com.yessorae.data.DataConstants.COL_TODO_ID
import com.yessorae.data.DataConstants.COL_UPPER_GOAL_ID
import com.yessorae.data.DataConstants.TABLE_GOAL
import com.yessorae.data.DataConstants.TABLE_TODO
import com.yessorae.data.local.database.model.GoalEntity
import com.yessorae.data.local.database.model.TodoEntity
import com.yessorae.util.getWeekRangePair
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TodoDao : BaseDao<TodoEntity> {
    @Query("SELECT * FROM $TABLE_TODO WHERE $COL_TODO_ID = :id")
    suspend fun loadTodoById(id: Int): TodoEntity

    @Query("SELECT * FROM $TABLE_TODO WHERE date BETWEEN :start AND :end")
    fun loadTodoFlow(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<TodoEntity>>

    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_GOAL_ID = :id 
        """
    )
    suspend fun loadGoalById(id: Int): GoalEntity

    @Update
    suspend fun updateGoal(upperGoal: GoalEntity)

    @Query(
        value = """
            SELECT * FROM $TABLE_TODO WHERE $COL_UPPER_GOAL_ID = :upperGoalId 
        """
    )
    suspend fun loadTodosByUpperGoalId(upperGoalId: Int): List<TodoEntity>

    @Query(
        value = """
            SELECT * FROM $TABLE_GOAL WHERE $COL_UPPER_GOAL_ID = :upperGoalId 
        """
    )
    suspend fun loadGoalsByUpperGoalId(upperGoalId: Int): List<GoalEntity>

    @Transaction
    suspend fun updateTodoTransaction(new: TodoEntity) {
        val old = loadTodoById(new.todoId)

        // 날짜만 바뀌었을 때 상위 목표 범위 벗어난 경우 상위 목표 제거.
        val newUpperGoalId = when {
            new.upperGoalId != old.upperGoalId -> new.upperGoalId

            new.date != old.date -> {
                new.upperGoalId?.let {
                    val oldWeekRangePair = old.date.date.getWeekRangePair()
                    if (new.date.date !in oldWeekRangePair.first..oldWeekRangePair.second) {
                        null
                    } else {
                        new.upperGoalId
                    }
                }
            }

            else -> {
                new.upperGoalId
            }
        }

        // 예전 상위 업데이트
        if (newUpperGoalId != old.upperGoalId) {
            old.upperGoalId?.let { oldUpperGoalId ->
                updateUpperGoalScoreTransaction(oldUpperGoalId)
            }
        }

        // 새로운 상위 업데이트
        newUpperGoalId?.let { upperGoalId ->
            updateUpperGoalScoreTransaction(upperGoalId = upperGoalId)
        }

        // 최종 투두 업데이트
        update(
            new.copy(
                upperGoalId = newUpperGoalId,
                upperGoalContributionScore = if (newUpperGoalId == null) {
                    null
                } else {
                    new.upperGoalContributionScore
                }
            )
        )
    }

    @Transaction
    suspend fun deleteTodoTransaction(deletedTodo: TodoEntity) {
        delete(deletedTodo)

        // 기존 상위 목표 삭제
        deletedTodo.upperGoalId?.let { upperGoalId ->
            updateUpperGoalScoreTransaction(upperGoalId = upperGoalId)
        }
    }

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
        updateGoal(
            upperGoal.copy(
                currentScore = todosScore + goalsScore
            )
        )

        // 상위 업데이트
        upperGoal.upperGoalId?.let {
            updateUpperGoalScoreTransaction(upperGoalId = it)
        }
    }

    @Transaction
    suspend fun checkTodoTransaction(todo: TodoEntity, upperGoal: GoalEntity?) {
        val newDone = todo.done.not()
        update(entity = todo.copy(done = newDone))

        upperGoal?.let {
            val newScore = upperGoal.currentScore + if (newDone) {
                todo.upperGoalContributionScore ?: 0
            } else {
                -(todo.upperGoalContributionScore ?: 0)
            }

            updateGoal(
                upperGoal = upperGoal.copy(
                    currentScore = newScore
                )
            )

            updateUpperGoalScoreByTodoCheckTransaction(goal = upperGoal, newScore = newScore)
        }
    }

    @Transaction
    suspend fun updateUpperGoalScoreByTodoCheckTransaction(goal: GoalEntity, newScore: Int) {
        // 상위 목표 있는지부터 체크
        goal.upperGoalId?.let { upperGoalId ->
            val upperGoal = loadGoalById(id = upperGoalId)

            // 달성 -> 미달성
            if (goal.done && goal.totalScore > newScore) {
                val newUpperGoalScore =
                    upperGoal.currentScore - (goal.upperGoalContributionScore ?: 0)
                updateGoal(
                    upperGoal = upperGoal.copy(
                        currentScore = newUpperGoalScore
                    )
                )

                updateUpperGoalScoreByTodoCheckTransaction(
                    goal = upperGoal,
                    newScore = newUpperGoalScore
                )
            }

            // 미달성 -> 달성
            if (goal.done.not() && goal.totalScore <= newScore) {
                val newUpperGoalScore =
                    upperGoal.currentScore + (goal.upperGoalContributionScore ?: 0)
                updateGoal(
                    upperGoal = upperGoal.copy(
                        currentScore = newUpperGoalScore
                    )
                )

                updateUpperGoalScoreByTodoCheckTransaction(
                    goal = upperGoal,
                    newScore = newUpperGoalScore
                )
            }
        }
    }
}
