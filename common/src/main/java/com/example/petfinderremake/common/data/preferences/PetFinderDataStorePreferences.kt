package com.example.petfinderremake.common.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.ext.orFalse
import com.example.petfinderremake.common.ext.orNotDefined
import com.example.petfinderremake.common.ext.rxDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class PetFinderDataStorePreferences @Inject constructor(
    @ApplicationContext val context: Context
) : Preferences {

    private val dataStore = context.rxDataStore()

    private val keyToken = stringPreferencesKey(PreferencesConstants.KEY_TOKEN)
    private val keyTokenExpirationTime =
        longPreferencesKey(PreferencesConstants.KEY_TOKEN_EXPIRATION_TIME)
    private val keyTokenType = stringPreferencesKey(PreferencesConstants.KEY_TOKEN_TYPE)
    private val keyPostcode = stringPreferencesKey(PreferencesConstants.KEY_POSTCODE)
    private val keyMaxDistance = intPreferencesKey(PreferencesConstants.KEY_MAX_DISTANCE)
    private val keyNotificationsPermanentlyDenied =
        booleanPreferencesKey(PreferencesConstants.KEY_NOTIFICATIONS_PERMANENTLY_DENIED)

    override fun putToken(token: String): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences[keyToken] = token
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    override fun getToken(): Flowable<String> {
        return dataStore.data().map { preferences -> preferences[keyToken].orEmpty() }
    }

    override fun putTokenExpirationTime(time: Long): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences[keyTokenExpirationTime] = time
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    override fun getTokenExpirationTime(): Flowable<Long> {
        return dataStore.data()
            .map { preferences -> preferences[keyTokenExpirationTime].orNotDefined() }
    }

    override fun putTokenType(tokenType: String): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences[keyTokenType] = tokenType
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    override fun getTokenType(): Flowable<String> {
        return dataStore.data()
            .map { preferences -> preferences[keyToken].orEmpty() }
    }

    override fun deleteTokenInfo(): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences.remove(keyToken)
            mutablePreferences.remove(keyTokenExpirationTime)
            mutablePreferences.remove(keyTokenType)
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    override fun putNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences[keyNotificationsPermanentlyDenied] = isPermanentlyDenied
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    override fun getNotificationPermanentlyDenied(): Flowable<Boolean> {
        return dataStore.data()
            .map { preferences -> preferences[keyNotificationsPermanentlyDenied].orFalse() }
    }
}