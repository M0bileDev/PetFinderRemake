package com.example.petfinderremake

import com.example.petfinderremake.common.data.preferences.FakePetFinderDataStorePreferences
import com.example.petfinderremake.common.domain.preferences.Preferences
import org.junit.Rule


abstract class PreferencesTest {

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    val preferences: Preferences = FakePetFinderDataStorePreferences()
}