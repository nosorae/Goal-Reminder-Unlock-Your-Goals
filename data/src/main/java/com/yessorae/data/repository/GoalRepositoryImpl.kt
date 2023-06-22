package com.yessorae.data.repository

import com.yessorae.common.Logger
import com.yessorae.data.local.database.dao.GoalDao
import com.yessorae.data.local.database.model.asDomainModel
import com.yessorae.data.local.database.model.asEntity
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.util.getWeekRangePair
import com.yessorae.util.toLocalDateTime
import com.yessorae.util.toStartLocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import java.time.YearMonth

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override fun getYearlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo impl
        Logger.dataDebug("getYearlyGoalsFlow : $day")
        val year = day.year
        val startDateTime = LocalDateTime(
            year = year,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0
        )
        val endDateTime = LocalDateTime(
            year = year,
            monthNumber = 12,
            dayOfMonth = 31,
            hour = 23,
            minute = 59,
            second = 59
        )

        return goalDao.loadGoalsFlow(
            start = startDateTime,
            end = endDateTime,
            goalType = GoalType.YEARLY.name
        ).map { list ->
            list.map {
                it.asDomainModel()
            }
        }
    }

    override fun getMonthlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo impl
        Logger.dataDebug("getMonthlyGoalsFlow : $day")

        val year = day.year
        val monthNumber = day.monthNumber
        val endDayOfMonth = YearMonth.of(year, monthNumber)
            .atEndOfMonth().toKotlinLocalDate().dayOfMonth
        Logger.dataDebug("getMonthlyGoalsFlow : year $year / monthNumber $monthNumber / endDayOfMonth $endDayOfMonth")

        val startDateTime = LocalDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0
        )
        val endDateTime = LocalDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = endDayOfMonth,
            hour = 23,
            minute = 59,
            second = 59
        )

        return goalDao.loadGoalsFlow(
            start = startDateTime,
            end = endDateTime,
            goalType = GoalType.MONTHLY.name
        ).map { list ->
            list.map {
                it.asDomainModel()
            }
        }
    }

    override fun getWeekdayGoalsFlow(day: LocalDateTime): Flow<List<Goal>> {
        val weekRangePair = day.date.getWeekRangePair()
        val startDateTime = weekRangePair.first.toStartLocalDateTime()
        val endDateTime = weekRangePair.second
            .toLocalDateTime(hour = 23, minute = 59, second = 59)

        return goalDao.loadGoalsFlow(
            start = startDateTime,
            end = endDateTime,
            goalType = GoalType.WEEKLY.name
        ).map { list ->
            list.map {
                it.asDomainModel()
            }
        }
    }

    override suspend fun getGoalById(goalId: Int): Goal {
        return goalDao.loadGoalById(goalId).asDomainModel()
    }

    override suspend fun insertGoal(goal: Goal): Int {
        return goalDao.insert(goal.asEntity()).toInt()
    }

    override suspend fun updateGoal(goal: Goal) {
        goalDao.update(goal.asEntity())
    }

    override suspend fun deleteGoalTransaction(goal: Goal) {
        goalDao.deleteGoalTransaction(goal.asEntity())
    }
}
