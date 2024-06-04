package com.example.petfinderremake.common.di

import com.example.petfinderremake.common.data.preferences.PetFinderDataStorePreferences
import com.example.petfinderremake.common.domain.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class PreferencesModule {

    @[Binds Singleton]
    abstract fun bindPreferences(
        petFinderDataStorePreferences: PetFinderDataStorePreferences
    ): Preferences
}