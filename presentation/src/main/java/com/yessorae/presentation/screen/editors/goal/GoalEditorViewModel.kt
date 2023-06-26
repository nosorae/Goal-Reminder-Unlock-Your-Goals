package com.yessorae.presentation.screen.editors.goal

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.model.type.toGoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.usecase.GetGoalWithUpperGoalUseCase
import com.yessorae.presentation.GoalEditorDestination
import com.yessorae.presentation.R
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.asDomainModel
import com.yessorae.presentation.model.asModel
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.TextString
import com.yessorae.util.getWeekRangePair
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import com.yessorae.util.toStartLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@HiltViewModel
class GoalEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val getGoalWithUpperGoalUseCase: GetGoalWithUpperGoalUseCase
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
        if (goalDayMilliSecParam != GoalEditorDestination.defaultGoalDayMilliSec) {
            val date = goalDayMilliSecParam.toLocalDateTime()
            updateState {
                stateValue.copy(
                    paramDate = date.date
                )
            }
        }

        updateState {
            stateValue.copy(
                paramGoalType = goalTypeParam.toGoalType()
            )
        }

        if (isUpdate) {
            val goalWithUpperGoal = getGoalWithUpperGoalUseCase(goalIdParam)
            val goal = goalWithUpperGoal.goal.asModel()
            val upperGoal = goalWithUpperGoal.upperGoal?.asModel()
            updateState {
                stateValue.copy(
                    goal = goal,
                    title = goal.title,
                    startDate = goal.startTime?.date,
                    endDate = goal.endTime?.date,
                    memo = goal.memo,
                    totalScore = goal.totalScore,
                    upperGoal = upperGoal,
                    upperGoalContributionScore = goal.upperGoalContributionScore
                )
            }
        }
    }

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                title = title,
                changed = true
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
                },
                changed = true
            )
        }
    }

    fun onChangeUpperGoalContributionScore(score: Int) {
        updateState {
            stateValue.copy(
                upperGoalContributionScore = score,
                changed = true
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
        if (stateValue.paramGoalType == GoalType.YEARLY) return@launch

        when (stateValue.paramGoalType) {
            GoalType.MONTHLY -> {
                goalRepository.getYearlyGoalsFlow(stateValue.paramDate.toStartLocalDateTime())
                    .firstOrNull()?.let { goals ->
                        if (goals.isEmpty()) {
                            _toast.emit(ResString(R.string.common_no_upper_goal))
                        } else {
                            val goalModels = goals.map { it.asModel() }
                            updateState {
                                stateValue.copy(
                                    editorDialogState = EditorDialogState.ContributeGoal(goalModels)
                                )
                            }
                        }
                    }
            }

            GoalType.WEEKLY -> {
                goalRepository
                    .getMonthlyGoalsFlow(stateValue.paramDate.toStartLocalDateTime())
                    .firstOrNull()?.let { goals ->
                        if (goals.isEmpty()) {
                            _toast.emit(ResString(R.string.common_no_upper_goal))
                        } else {
                            val goalModels = goals.map { it.asModel() }
                            updateState {
                                stateValue.copy(
                                    editorDialogState = EditorDialogState.ContributeGoal(goalModels)
                                )
                            }
                        }
                    }
            }

            else -> {
                // do nothing
            }
        }
    }

    fun onClickBack() = ioScope.launch {
        if (stateValue.changed) {
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
                if (paramDate.monthNumber != date.monthNumber) {
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
                if (date !in (range.first..range.second)) {
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
                        startDate = date,
                        changed = true
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
                        endDate = date,
                        changed = true
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
                upperGoal = goal,
                upperGoalContributionScore = 0,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onSelectNoneGoal() {
        updateState {
            stateValue.copy(
                upperGoal = null,
                upperGoalContributionScore = null,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onChangeMemo(memo: String) {
        updateState {
            stateValue.copy(
                memo = memo,
                changed = true
            )
        }
    }

    fun onClickSave() = ioScope.launch {
        stateValue.getUpdatedGoal()?.asDomainModel()?.let {
            showLoading()
            if (isUpdate) {
                goalRepository.updateGoalTransaction(it)
            } else {
                goalRepository.insertGoal(it)
            }
            hideLoading()
            back()
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
    val upperGoal: GoalModel? = null,
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null,
    val changed: Boolean = false,
    val editorDialogState: EditorDialogState = EditorDialogState.None
) {
    val enableSaveButton by lazy {
        title != null && totalScore != null
    }

    val isUpdate by lazy {
        goal != null
    }

    val showGoalListItem by lazy {
        paramGoalType != GoalType.YEARLY
    }

    val toolbarTitle: StringModel by lazy {
        when (paramGoalType) {
            GoalType.WEEKLY -> {
                if (isUpdate) {
                    ResString(R.string.goal_edit_toolbar_title_weekly)
                } else {
                    ResString(R.string.goal_add_toolbar_title_weekly)
                }
            }

            GoalType.MONTHLY -> {
                if (isUpdate) {
                    ResString(R.string.goal_edit_toolbar_title_monthly)
                } else {
                    ResString(R.string.goal_add_toolbar_title_monthly)
                }
            }

            GoalType.YEARLY -> {
                if (isUpdate) {
                    ResString(R.string.goal_edit_toolbar_title_yearly)
                } else {
                    ResString(R.string.goal_add_toolbar_title_yearly)
                }
            }

            GoalType.NONE -> {
                TextString("")
            }
        }
    }

    val dayEditorTitle: StringModel? by lazy {
        when (paramGoalType) {
            GoalType.WEEKLY -> {
                val weekRangePair = paramDate.getWeekRangePair()
                val first = weekRangePair.first
                val last = weekRangePair.second
                ResString(
                    R.string.goal_weekly_range,
                    first.monthNumber,
                    first.dayOfMonth,
                    last.monthNumber,
                    last.dayOfMonth
                )
            }

            GoalType.MONTHLY -> {
                ResString(
                    R.string.goal_monthly_range,
                    paramDate.year,
                    paramDate.monthNumber
                )
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
        if (enableSaveButton.not()) return null

        val goalTitle = title
        val goalTotalScore = totalScore ?: goal?.totalScore

        return if (goalTitle != null && goalTotalScore != null) {
            goal?.copy(
                title = goalTitle,
                dateFrom = paramDate.toStartLocalDateTime(),
                startTime = startDate?.toStartLocalDateTime(),
                endTime = endDate?.toStartLocalDateTime(),
                totalScore = goalTotalScore,
                upperGoalId = upperGoal?.goalId,
                upperGoalContributionScore = upperGoalContributionScore,
                memo = memo
            ) ?: GoalModel(
                title = goalTitle,
                dateFrom = paramDate.toStartLocalDateTime(),
                startTime = startDate?.toStartLocalDateTime(),
                endTime = endDate?.toStartLocalDateTime(),
                totalScore = goalTotalScore,
                currentScore = 0,
                upperGoalId = upperGoal?.goalId,
                upperGoalContributionScore = upperGoalContributionScore,
                memo = memo,
                type = paramGoalType
            )
        } else {
            null
        }
    }
}
