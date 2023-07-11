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
import com.yessorae.domain.model.type.GoalType
import com.yessorae.util.getMonthRangePair
import com.yessorae.util.getWeekRangePair
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

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    // todo async 고려
    @Transaction
    suspend fun updateGoalTransaction(new: GoalEntity) {
        val old = loadGoalById(new.goalId)

        // 현재 목표의 날짜만 바뀌었을 때 상위 목표 범위 벗어났다면 제거
        val newUpperGoalId = when {
            new.upperGoalId != old.upperGoalId -> new.upperGoalId

            new.dateFrom != old.dateFrom -> {
                val oldDate = old.dateFrom.date
                val newDate = new.dateFrom.date
                when (old.type) {
                    GoalType.MONTHLY -> {
                        if (oldDate.year != newDate.year) {
                            null
                        } else {
                            new.upperGoalId
                        }
                    }

                    GoalType.WEEKLY -> {
                        val oldMonthRangePair = oldDate.getMonthRangePair()
                        if (newDate !in oldMonthRangePair.first..oldMonthRangePair.second) {
                            null
                        } else {
                            new.upperGoalId
                        }
                    }

                    else -> {
                        new.upperGoalId
                    }
                }
            }

            else -> {
                new.upperGoalId
            }
        }

        // 현재 목표의 하위 투두/목표들 범위 체크
        val lowerTodos = loadTodosByUpperGoalId(new.goalId)
        val lowerGoals = loadGoalsByUpperGoalId(new.goalId)
        val weekRangePair = new.dateFrom.date.getWeekRangePair()
        var totalScore = 0
        lowerTodos.forEach {
            if (it.date.date !in weekRangePair.first..weekRangePair.second) {
                updateTodo(
                    it.copy(
                        upperGoalId = null,
                        upperGoalContributionScore = null
                    )
                )
            } else {
                if (it.done) {
                    totalScore += it.upperGoalContributionScore ?: 0
                }
            }
        }
        lowerGoals.forEach {
            when (new.type) {
                GoalType.YEARLY -> {
                    if (it.dateFrom.year != new.dateFrom.year) {
                        update(
                            it.copy(
                                upperGoalId = null,
                                upperGoalContributionScore = null
                            )
                        )
                    } else {
                        if (it.done) {
                            totalScore += it.upperGoalContributionScore ?: 0
                        }
                    }
                }

                GoalType.MONTHLY -> {
                    if (
                        it.dateFrom.monthNumber != new.dateFrom.monthNumber ||
                        it.dateFrom.year != new.dateFrom.year
                    ) {
                        update(
                            it.copy(
                                upperGoalId = null,
                                upperGoalContributionScore = null
                            )
                        )
                    } else {
                        if (it.done) {
                            totalScore += it.upperGoalContributionScore ?: 0
                        }
                    }
                }

                else -> {
                    // do nothing
                }
            }
        }

        // 현재 목표 먼저 업데이트
        update(
            new.copy(
                currentScore = totalScore,
                upperGoalId = newUpperGoalId,
                upperGoalContributionScore = if (newUpperGoalId == null) {
                    null
                } else {
                    new.upperGoalContributionScore
                }
            )
        )

        // 그 다음에 현재 목표의, "예전" 상위 목표 업데이트
        if (newUpperGoalId != old.upperGoalId) {
            old.upperGoalId?.let { oldUpperGoalId ->
                updateUpperGoalScoreTransaction(upperGoalId = oldUpperGoalId)
            }
        }

        // 그 다음에 현재 목표의, "새로운" 상위 목표 업데이트
        newUpperGoalId?.let {
            updateUpperGoalScoreTransaction(upperGoalId = it)
        }
    }

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

        // 현재 목표 점수 먼저 업데이트 후
        update(
            upperGoal.copy(
                currentScore = todosScore + goalsScore
            )
        )

        // 상위 목표 업데이트
        upperGoal.upperGoalId?.let {
            updateUpperGoalScoreTransaction(upperGoalId = it)
        }
    }
}
