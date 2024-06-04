package com.example.petfinderremake.common.data.preferences

import com.example.petfinderremake.common.domain.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePetFinderDataStorePreferences : Preferences {

    private var fakeToken = ""
    private var fakeTokenExpirationTime = -1L
    private var fakeTokenType = ""
    private var fakeNotificationsPermanentlyDenied: Boolean = false

    override suspend fun putToken(token: String) {
        fakeToken = token
    }

    override suspend fun putTokenExpirationTime(time: Long) {
        fakeTokenExpirationTime = time
    }

    override suspend fun putTokenType(tokenType: String) {
        fakeTokenType = tokenType
    }

    override suspend fun deleteTokenInfo() {
        fakeToken = ""
        fakeTokenType = ""
        fakeTokenExpirationTime = -1L
    }

    override suspend fun putNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean) {
        fakeNotificationsPermanentlyDenied = isPermanentlyDenied
    }

    override fun getToken(): Flow<String> {
        return flow { emit(fakeToken) }
    }

    override fun getTokenExpirationTime(): Flow<Long> {
        return flow { emit(fakeTokenExpirationTime) }
    }

    override fun getTokenType(): Flow<String> {
        return flow { emit(fakeTokenType) }
    }

    override fun getNotificationPermanentlyDenied(): Flow<Boolean> {
        return flow { emit(fakeNotificationsPermanentlyDenied) }
    }

}