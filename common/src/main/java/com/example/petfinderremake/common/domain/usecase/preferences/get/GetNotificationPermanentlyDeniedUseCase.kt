package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.common.domain.preferences.Preferences
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetNotificationPermanentlyDeniedUseCase @Inject constructor(
    private val preferences: Preferences
) {
    operator fun invoke(): Flowable<Result<Boolean, NotYetDefinedError>> {
        return preferences
            .getNotificationPermanentlyDenied()
            .map { Result.Success(it) }
    }
}