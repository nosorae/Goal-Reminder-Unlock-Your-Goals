package com.yessorae.goalreminder.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.yessorae.common.Logger
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.usecase.CheckTodoTransactionUseCase
import com.yessorae.goalreminder.background.periodicalarm.PeriodicNotificationReceiver
import com.yessorae.presentation.R
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.datetime.LocalDateTime
import java.util.Calendar

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
                minutesAlarmSetting()
            } catch (e: Exception) {
                Logger.recordException(e)
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

        val yearlyGoalId1 = goalRepository.insertGoal(goal = yearlyGoal1)

        val monthlyGoal1 = Goal(
            title = context.getString(R.string.mock_monthly_goal_title_1),
            dateFrom = now,
            type = GoalType.MONTHLY,
            upperGoalId = yearlyGoalId1,
            upperGoalContributionScore = 1
        )
        val monthlyGoalId1 = goalRepository.insertGoal(goal = monthlyGoal1)

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
        val weeklyGoalId1_1 = goalRepository.insertGoal(weeklyGoal1_1)
        val weeklyGoalId1_2 = goalRepository.insertGoal(weeklyGoal1_2)

        val todo1_1 = Todo(
            title = context.getString(R.string.mock_todo_title_1_1),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId1_1,
            upperGoalContributionScore = 20
        )
        val todo1_2 = Todo(
            title = context.getString(R.string.mock_todo_title_1_2),
            date = now,
            startTime = now.date.toLocalDateTime(hour = 13),
            endTime = now.date.toLocalDateTime(hour = 17),
            upperGoalId = weeklyGoalId1_2,
            upperGoalContributionScore = 20
        )
        val todo3 = Todo(
            title = context.getString(R.string.mock_todo_title_3),
            date = now,
            startTime = now.date.toLocalDateTime(),
            endTime = now.date.toLocalDateTime()
        )
        todoRepository.insertTodo(todo1_1)
        val todoId1_2 = todoRepository.insertTodo(todo1_2)

        todoRepository.insertTodo(todo3)
        checkTodoTransactionUseCase(
            TodoWithGoal(
                todo = todo1_2.copy(todoId = todoId1_2),
                upperGoal = weeklyGoal1_2.copy(goalId = weeklyGoalId1_2)
            )
        )
    }

    private fun yearlyAlarmSetting() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PeriodicNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun minutesAlarmSetting() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PeriodicNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
