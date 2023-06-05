package com.yessorae.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.yessorae.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferenceDataSore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.datastore
}
