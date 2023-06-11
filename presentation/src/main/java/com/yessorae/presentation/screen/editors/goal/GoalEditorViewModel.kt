package com.yessorae.presentation.screen.editors.goal

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
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
import com.yessorae.util.now
import com.yessorae.util.toDefaultLocalDateTime
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import javax.inject.Inject

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
                    startDay = model.startTime?.date,
                    endDay = model.endTime?.date
                )
            }
        }

        if (goalDayMilliSecParam != GoalEditorDestination.defaultGoalDayMilliSec) {
            val date = goalDayMilliSecParam.toDefaultLocalDateTime()
            updateState {
                stateValue.copy(
                    paramDate = date.date
                )
            }
        }

        val goalType = goalTypeParam.toGoalType()
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

    fun onClickStartDate() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.Date
            )
        }
    }

    fun onClickEndDate() {
        updateState {
            stateValue.copy(

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

    fun onClickSave() = ioScope.launch {
        stateValue.getGoal()?.asDomainModel()?.let {
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
    val startDay: LocalDate? = null,
    val endDay: LocalDate? = null,
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

    fun getGoal(): GoalModel? {
        val goal = goal

        val goalTitle = title ?: goal?.title

        val startDate = startDay?.let { startDay ->
            paramDate.minus(paramDate.dayOfMonth - startDay.dayOfMonth, DateTimeUnit.DAY)
                .toDefaultLocalDateTime()
        } ?: goal?.startTime

        val endDate = endDay?.let { endDay ->
            paramDate.plus(endDay.dayOfMonth - paramDate.dayOfMonth, DateTimeUnit.DAY).toDefaultLocalDateTime()
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
