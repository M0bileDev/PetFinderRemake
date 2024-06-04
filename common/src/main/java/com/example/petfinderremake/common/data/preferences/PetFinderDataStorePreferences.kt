package com.example.petfinderremake.common.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.ext.dataStore
import com.example.petfinderremake.common.ext.orFalse
import com.example.petfinderremake.common.ext.orNotDefined
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetFinderDataStorePreferences @Inject constructor(
    @ApplicationContext val context: Context
) : Preferences {

    private val keyToken = stringPreferencesKey(PreferencesConstants.KEY_TOKEN)
    private val keyTokenExpirationTime =
        longPreferencesKey(PreferencesConstants.KEY_TOKEN_EXPIRATION_TIME)
    private val keyTokenType = stringPreferencesKey(PreferencesConstants.KEY_TOKEN_TYPE)
    private val keyPostcode = stringPreferencesKey(PreferencesConstants.KEY_POSTCODE)
    private val keyMaxDistance = intPreferencesKey(PreferencesConstants.KEY_MAX_DISTANCE)
    private val keyNotificationsPermanentlyDenied =
        booleanPreferencesKey(PreferencesConstants.KEY_NOTIFICATIONS_PERMANENTLY_DENIED)

    override suspend fun putToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[keyToken] = token
        }
    }

    override fun getToken(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[keyToken].orEmpty()
            }
    }

    override suspend fun putTokenExpirationTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[keyTokenExpirationTime] = time
        }
    }

    override fun getTokenExpirationTime(): Flow<Long> {
        return context.dataStore.data
            .map { preferences ->
                preferences[keyTokenExpirationTime].orNotDefined()
            }
    }

    override suspend fun putTokenType(tokenType: String) {
        context.dataStore.edit { preferences ->
            preferences[keyTokenType] = tokenType
        }
    }

    override fun getTokenType(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[keyToken].orEmpty()
            }
    }

    override suspend fun deleteTokenInfo() {
        context.dataStore.edit {
            it.remove(keyToken)
            it.remove(keyTokenExpirationTime)
            it.remove(keyTokenType)
        }
    }

    override suspend fun putNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[keyNotificationsPermanentlyDenied] = isPermanentlyDenied
        }
    }

    override fun getNotificationPermanentlyDenied(): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[keyNotificationsPermanentlyDenied].orFalse()
            }
    }
}