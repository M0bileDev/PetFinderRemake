package com.example.petfinderremake.common.domain.usecase.preferences.delete

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DeleteTokenInfoUseCase @Inject constructor(
    private val preferences: Preferences
) {

    operator fun invoke(): Single<Result<Unit, NotYetDefinedError>> {
        return preferences.deleteTokenInfo().map { Result.Success(Unit) }
    }
}