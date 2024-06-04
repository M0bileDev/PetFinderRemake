package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNotificationPermanentlyDeniedUseCase @Inject constructor(
    private val preferences: Preferences
) {
    suspend operator fun invoke(): Flow<Result<Boolean, NotYetDefinedError>> {
        return withContext(Dispatchers.IO) {
            preferences.getNotificationPermanentlyDenied().map { Result.Success(it) }
        }
    }
}