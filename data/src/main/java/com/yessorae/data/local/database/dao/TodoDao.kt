package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yessorae.data.Constants.COL_GOAL_ID
import com.yessorae.data.Constants.COL_TODO_ID
import com.yessorae.data.Constants.TABLE_GOAL
import com.yessorae.data.Constants.TABLE_TODO
import com.yessorae.data.local.database.model.GoalEntity
import com.yessorae.data.local.database.model.TodoEntity
import com.yessorae.domain.model.Goal
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


    @Update
    suspend fun updateUpperGoal(upperGoal: GoalEntity)

    @Transaction
    suspend fun checkTodo(todoEntity: TodoEntity, goalEntity: GoalEntity?) {
        val newDone = todoEntity.done.not()
        update(entity = todoEntity.copy(done = newDone))
        goalEntity?.let {
            updateUpperGoal(
                upperGoal = goalEntity.copy(
                    currentScore = goalEntity.currentScore + if (newDone) {
                        todoEntity.upperGoalContributionScore ?: 0
                    } else {
                        -(todoEntity.upperGoalContributionScore ?: 0)
                    }
                )
            )
        }
    }
}