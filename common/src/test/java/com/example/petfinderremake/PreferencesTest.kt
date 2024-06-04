package com.example.petfinderremake

import com.example.petfinderremake.common.data.preferences.FakePetFinderDataStorePreferences
import com.example.petfinderremake.common.domain.preferences.Preferences

abstract class PreferencesTest {

    val preferences: Preferences = FakePetFinderDataStorePreferences()
}