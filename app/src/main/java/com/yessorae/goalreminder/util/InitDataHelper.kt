package com.yessorae.goalreminder.util

import android.content.Context
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.usecase.CheckTodoTransactionUseCase
import com.yessorae.presentation.R
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class InitDataHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository,
    private val todoRepository: TodoRepository,
    private val goalRepository: GoalRepository,
    private val checkTodoTransactionUseCase: CheckTodoTransactionUseCase
) {
    suspend fun processOnBoardingData() {
        if (preferencesDatastoreRepository.getCompleteOnBoardingMockData().not()) {
            preferencesDatastoreRepository.setCompleteOnBoardingMockData()
            try {
                insertOnBoardingData()
            } catch (e: Exception) {
                Timber.tag("SR-N").e("InitDataHelper processOnBoardingData : $e")
            }
        }
    }

    private suspend fun insertOnBoardingData() {
        val now = LocalDateTime.now()
        val yearlyGoal1 = Goal(
            title = context.getString(R.string.mock_yearly_goal_title_1),
            dateFrom = now,
            type = GoalType.YEARLY,
            totalScore = 12
        )
        val yearlyGoal2 = Goal(
            title = context.getString(R.string.mock_yearly_goal_title_2),
            dateFrom = now,
            type = GoalType.YEARLY
        )
        val yearlyGoalId1 = goalRepository.insertGoal(goal = yearlyGoal1)
        val yearlyGoalId2 = goalRepository.insertGoal(goal = yearlyGoal2)

        val monthlyGoal1 = Goal(
            title = context.getString(R.string.mock_monthly_goal_title_1),
            dateFrom = now,
            type = GoalType.MONTHLY,
            upperGoalId = yearlyGoalId1,
            upperGoalContributionScore = 1
        )
        val monthlyGoal2 = Goal(
            title = context.getString(R.string.mock_monthly_goal_title_2),
            dateFrom = now,
            type = GoalType.MONTHLY,
            upperGoalId = yearlyGoalId2,
            upperGoalContributionScore = 50
        )
        val monthlyGoalId1 = goalRepository.insertGoal(goal = monthlyGoal1)
        val monthlyGoalId2 = goalRepository.insertGoal(goal = monthlyGoal2)

        val weeklyGoal1_1 = Goal(
            title = context.getString(R.string.mock_weekly_goal_title_1_1),
            dateFrom = now,
            type = GoalType.WEEKLY,
            upperGoalId = monthlyGoalId1,
            upperGoalContributionScore = 15
        )
        val weeklyGoal1_2 = Goal(
            title = context.getString(R.string.mock_weekly_goal_title_1_2),
            dateFrom = now,
            type = GoalType.WEEKLY,
            upperGoalId = monthlyGoalId1,
            upperGoalContributionScore = 5
        )
        val weeklyGoal2 = Goal(
            title = context.getString(R.string.mock_weekly_goal_title_2),
            dateFrom = now,
            type = GoalType.WEEKLY,
            upperGoalId = monthlyGoalId2,
            upperGoalContributionScore = 100
        )
        val weeklyGoalId1_1 = goalRepository.insertGoal(weeklyGoal1_1)
        val weeklyGoalId1_2 = goalRepository.insertGoal(weeklyGoal1_2)
        val weeklyGoalId2 = goalRepository.insertGoal(weeklyGoal2)

        val todo1_1 = Todo(
            title = context.getString(R.string.mock_todo_title_1_1),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId1_1,
            upperGoalContributionScore = 20,
        )
        val todo1_2 = Todo(
            title = context.getString(R.string.mock_todo_title_1_2),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId1_2,
            upperGoalContributionScore = 20,
        )
        val todo2_1 = Todo(
            title = context.getString(R.string.mock_todo_title_2_1),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId2,
            upperGoalContributionScore = 50,
        )
        val todo2_2 = Todo(
            title = context.getString(R.string.mock_todo_title_2_2),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId2,
            upperGoalContributionScore = 50,
        )
        val todo3 = Todo(
            title = context.getString(R.string.mock_todo_title_3),
            date = now,
            startTime = now.date.toLocalDateTime(),
            endTime = now.date.toLocalDateTime()
        )
        todoRepository.insertTodo(todo1_1)
        todoRepository.insertTodo(todo1_2)
        val todoId2_1 = todoRepository.insertTodo(todo2_1)
        val todoId2_2 = todoRepository.insertTodo(todo2_2)
        todoRepository.insertTodo(todo3)
        checkTodoTransactionUseCase(
            TodoWithGoal(
                todo = todo2_1.copy(todoId = todoId2_1),
                upperGoal = weeklyGoal2.copy(goalId = weeklyGoalId2)
            )
        )

        checkTodoTransactionUseCase(
            TodoWithGoal(
                todo = todo2_2.copy(todoId = todoId2_2),
                upperGoal = weeklyGoal2.copy(goalId = weeklyGoalId2)
            )
        )
    }

}