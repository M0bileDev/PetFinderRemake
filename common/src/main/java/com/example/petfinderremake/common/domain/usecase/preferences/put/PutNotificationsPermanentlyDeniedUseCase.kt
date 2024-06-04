package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PutNotificationsPermanentlyDeniedUseCase @Inject constructor(
    private val preferences: Preferences
) {
    suspend operator fun invoke(granted: Boolean): Result<Unit, NotYetDefinedError> {
        return withContext(Dispatchers.IO) {
            preferences.putNotificationsPermanentlyDenied(granted).run { Result.Success(Unit) }
        }
    }
}