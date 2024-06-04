package com.example.petfinderremake.common.domain.usecase.preferences.delete

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteTokenInfoUseCase @Inject constructor(
    private val preferences: Preferences
) {

    suspend operator fun invoke(): Result<Unit, NotYetDefinedError> {
        return withContext(Dispatchers.IO) {
            preferences.deleteTokenInfo().run { Result.Success(Unit) }
        }
    }
}