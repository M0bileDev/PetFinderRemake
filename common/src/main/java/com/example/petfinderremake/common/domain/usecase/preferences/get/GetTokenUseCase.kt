package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val preferences: Preferences
) {
    operator fun invoke(): Flowable<Result<String, NotYetDefinedError>> {
        return preferences.getToken().map { Result.Success(it) }
    }
}