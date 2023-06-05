package com.yessorae.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.yessorae.data.Constants.PREF_KEY_IS_SCREEN_ON
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDatastoreRepositoryImpl @Inject constructor(
    private val preferences: DataStore<Preferences>
) : PreferencesDatastoreRepository {
    private val IS_SERVICE_ON = booleanPreferencesKey(PREF_KEY_IS_SCREEN_ON)

    override val isServiceOn: Flow<Boolean> = preferences.data.map { preferences ->
        preferences[IS_SERVICE_ON] ?: false
    }

    override suspend fun setServiceOnOff(on: Boolean) {
        preferences.edit { settings ->
            settings[IS_SERVICE_ON] = on
        }
    }
}
