package com.yessorae.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesDatastoreRepository {
    val isServiceOn: Flow<Boolean>
    suspend fun setServiceOnOff(on: Boolean)
}
