package com.yessorae.domain.usecase

import com.yessorae.domain.model.FinalGoal
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetFinalGoalUseCase @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) {
    operator fun invoke(): Flow<FinalGoal> {
        return combine(
            preferencesDatastoreRepository.getFinalGoalYear(),
            preferencesDatastoreRepository.getFinalGoal()
        ) { year, text ->
            year to text
        }.map { (year, text) ->
            FinalGoal(
                year = year,
                text = text
            )
        }
    }
}
