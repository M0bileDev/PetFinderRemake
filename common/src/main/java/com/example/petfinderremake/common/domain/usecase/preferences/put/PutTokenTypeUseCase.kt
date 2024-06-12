package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PutTokenTypeUseCase @Inject constructor(
    private val preferences: Preferences
) {
    operator fun invoke(tokenType: String): Single<Result<Unit, NotYetDefinedError>> {
        return preferences.putTokenType(tokenType).map { Result.Success(Unit) }
    }
}