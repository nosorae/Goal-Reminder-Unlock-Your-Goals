package com.yessorae.domain.usecase

import com.yessorae.domain.model.FinalGoal
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject

class UpdateFinalGoalUseCase @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) {
    suspend operator fun invoke(finalGoal: FinalGoal) {
        preferencesDatastoreRepository.setFinalGoal(finalGoal.text)
        preferencesDatastoreRepository.setFinalGoalYear(finalGoal.year)
    }
}
