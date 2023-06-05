package com.yessorae.di

import com.yessorae.data.local.preference.PreferencesDatastoreRepositoryImpl
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindPreferencesDatastoreRepository(preferenceDataStoreRepository: PreferencesDatastoreRepositoryImpl): PreferencesDatastoreRepository
}
