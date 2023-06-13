package com.yessorae.presentation.screen.editors.goal

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.common.Logger
import com.yessorae.domain.model.enum.GoalType
import com.yessorae.domain.model.enum.toGoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.presentation.GoalEditorDestination
import com.yessorae.presentation.R
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.asDomainModel
import com.yessorae.presentation.model.asModel
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.getWeekRange
import com.yessorae.util.getWeekRangePair
import com.yessorae.util.now
import com.yessorae.util.toDefaultLocalDateTime
import com.yessorae.util.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel
class GoalEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository
) : BaseScreenViewModel<GoalEditorScreenState>() {
    private val goalIdParam: Int =
        checkNotNull(savedStateHandle[GoalEditorDestination.goalIdArg])
    private val isUpdate: Boolean by lazy {
        goalIdParam != GoalEditorDestination.defaultGoalId
    }
    private val goalDayMilliSecParam: Long =
        checkNotNull(savedStateHandle[GoalEditorDestination.goalDayMilliSecArg])
    private val goalTypeParam: String =
        checkNotNull(savedStateHandle[GoalEditorDestination.goalTypeArg])

    init {
        initStateValue()
    }

    private fun initStateValue() = ioScope.launch {
        if (isUpdate) {
            val model = goalRepository.getGoalById(goalIdParam).asModel()
            updateState {
                stateValue.copy(
                    goal = model,
                    title = model.title,
                    startDate = model.startTime?.date,
                    endDate = model.endTime?.date
                )
            }
        }

        Logger.uiDebug("goalDayMilliSecParam ${goalDayMilliSecParam} /  defaultGoalDayMilliSec ${GoalEditorDestination.defaultGoalDayMilliSec}")
        if (goalDayMilliSecParam != GoalEditorDestination.defaultGoalDayMilliSec) {
            Logger.uiError("goalDayMilliSecParam ${goalDayMilliSecParam} /  defaultGoalDayMilliSec ${GoalEditorDestination.defaultGoalDayMilliSec}")

            val date = goalDayMilliSecParam.toLocalDateTime() // todo fix bug
            updateState {
                stateValue.copy(
                    paramDate = date.date
                )
            }
        }

        val goalType = goalTypeParam.toGoalType()
        Logger.uiDebug("goalType ${goalType} /  goalTypeParam ${goalTypeParam}")

        updateState {
            stateValue.copy(
                paramGoalType = goalType
            )
        }
    }

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                title = title
            )
        }
    }

    fun onChangeTotalScore(
        totalScore: String
    ) {
        updateState {
            stateValue.copy(
                totalScore = try {
                    totalScore.toInt()
                } catch (e: Exception) {
                    null
                    // crashlytics
                }
            )
        }
    }

    fun onChangeContributionScore(score: Int) {
        updateState {
            stateValue.copy(
                contributionScore = score
            )
        }
    }

    fun onClickStartDate() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.StartDate
            )
        }
    }

    fun onClickEndDate() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.EndDate
            )
        }
    }

    fun onClickContributeGoal() = ioScope.launch {
        goalRepository
            .getWeekdayGoalsFlow(stateValue.paramDate.toDefaultLocalDateTime())
            .collectLatest { goals ->
                val goalModels = goals.map { it.asModel() }
                updateState {
                    stateValue.copy(
                        editorDialogState = EditorDialogState.ContributeGoal(goalModels)
                    )
                }
            }
    }

    fun onClickBack() = ioScope.launch {
        if (stateValue.enableSaveButton) {
            updateState {
                stateValue.copy(
                    editorDialogState = EditorDialogState.ExitConfirm
                )
            }
        } else {
            back()
        }
    }

    fun onConfirmBack() = ioScope.launch {
        onCancelDialog()
        back()
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.None
            )
        }
    }

    fun onSelectDate(milliSec: Long, dialogState: EditorDialogState) = ioScope.launch {
        val date = milliSec.toLocalDateTime().date
        val paramDate = stateValue.paramDate


        when (stateValue.paramGoalType) {
            GoalType.YEARLY -> {
                if (paramDate.year != date.year) {
                    _toast.emit(
                        ResString(
                            R.string.goal_toast_out_of_range_year,
                            paramDate.year
                        )
                    )
                    return@launch
                }
            }

            GoalType.MONTHLY -> {
                if (paramDate.monthNumber != date.dayOfMonth) {
                    _toast.emit(
                        ResString(
                            R.string.goal_toast_out_of_range_month,
                            paramDate.monthNumber
                        )
                    )
                    return@launch
                }

            }

            GoalType.WEEKLY -> {
                val range = paramDate.getWeekRangePair()
                Logger.uiDebug("date.dayOfMonth ${date.dayOfMonth} / stateValue.paramDate.getWeekRange() ${stateValue.paramDate.getWeekRange()} ")
                Logger.uiDebug("range.first.dayOfMonth ${range.first.dayOfMonth} / range.second.dayOfMonth ${range.second.dayOfMonth}")
                if (date.dayOfMonth in (range.first.dayOfMonth..range.second.dayOfMonth)) {
                    _toast.emit(
                        ResString(
                            R.string.goal_toast_out_of_range_week,
                            range.first.monthNumber,
                            range.first.dayOfMonth,
                            range.second.monthNumber,
                            range.second.dayOfMonth
                        )
                    )
                    return@launch
                }
            }

            else -> {
                // do nothing
            }
        }

        when (dialogState) {
            is EditorDialogState.StartDate -> {
                val endDate = stateValue.endDate
                if (endDate != null && endDate < date) {
                    _toast.emit(ResString(R.string.goal_toast_out_of_order_start))
                    return@launch
                }
                updateState {
                    stateValue.copy(
                        startDate = date
                    )
                }
            }

            is EditorDialogState.EndDate -> {
                val startDate = stateValue.startDate
                if (startDate != null && startDate > date) {
                    _toast.emit(ResString(R.string.goal_toast_out_of_order_end))
                    return@launch
                }
                updateState {
                    stateValue.copy(
                        endDate = date
                    )
                }
            }

            else -> {
                // do nothing
            }
        }

        onCancelDialog()
    }

    fun onSelectContributeGoal(goal: GoalModel) {
        updateState {
            stateValue.copy(
                contributionGoal = goal,
                contributionScore = 0
            )
        }
        onCancelDialog()
    }

    fun onSelectNoneGoal() {
        updateState {
            stateValue.copy(
                contributionGoal = null,
                contributionScore = 0
            )
        }
        onCancelDialog()
    }

    fun onClickSave() = ioScope.launch {
        stateValue.getUpdatedGoal()?.asDomainModel()?.let {
            if (isUpdate) {
                goalRepository.updateGoal(it)
            } else {
                goalRepository.insertGoal(it)
            }
        }


    }

    private suspend fun back() {
        _navigationEvent.emit(null)
    }

    override fun createInitialState(): GoalEditorScreenState {
        return GoalEditorScreenState()
    }
}

data class GoalEditorScreenState(
    val goal: GoalModel? = null,
    val paramDate: LocalDate = LocalDateTime.now().date,
    val paramGoalType: GoalType = GoalType.NONE,
    val title: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val totalScore: Int? = 100,
    val contributionGoal: GoalModel? = null,
    val contributionScore: Int? = null,
    val editorDialogState: EditorDialogState = EditorDialogState.None
) {
    val enableSaveButton by lazy {
        title != null && totalScore != null
    }

    val isUpdate by lazy {
        goal != null
    }

    val dayEditorTitle: StringModel? by lazy {
        when (paramGoalType) {
            GoalType.WEEKLY -> {
                ResString(
                    R.string.goal_weekly_range,
                    paramDate.getWeekRange().first,
                    paramDate.getWeekRange().last
                )
            }

            GoalType.MONTHLY -> {
                ResString(R.string.goal_monthly_range, paramDate.monthNumber)
            }

            GoalType.YEARLY -> {
                ResString(R.string.goal_yearly_range, paramDate.year)
            }

            GoalType.NONE -> {
                null
            }
        }
    }

    fun getUpdatedGoal(): GoalModel? {
        val goal = goal

        val goalTitle = title ?: goal?.title

        val startDate = startDate?.let { startDay ->
            paramDate.minus(paramDate.dayOfMonth - startDay.dayOfMonth, DateTimeUnit.DAY)
                .toDefaultLocalDateTime()
        } ?: goal?.startTime

        val endDate = endDate?.let { endDay ->
            paramDate.plus(endDay.dayOfMonth - paramDate.dayOfMonth, DateTimeUnit.DAY)
                .toDefaultLocalDateTime()
        } ?: goal?.endTime

        val goalTotalScore = totalScore ?: goal?.totalScore

        val contributionGoal = contributionGoal?.contributeGoalId ?: goal?.contributeGoalId

        val contributeScore = contributionScore ?: goal?.contributeScore

        return if (goalTitle != null && goalTotalScore != null) {
            goal?.copy(
                title = goalTitle,
                startTime = startDate,
                endTime = endDate,
                totalScore = goalTotalScore,
                contributeGoalId = contributionGoal,
                contributeScore = contributeScore
            ) ?: GoalModel(
                title = goalTitle,
                startTime = startDate,
                endTime = endDate,
                totalScore = goalTotalScore,
                currentScore = 0,
                contributeGoalId = contributionGoal,
                contributeScore = contributeScore,
                type = paramGoalType
            )
        } else {
            null
        }
    }

}
