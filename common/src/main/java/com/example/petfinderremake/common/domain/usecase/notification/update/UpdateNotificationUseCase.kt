package com.example.petfinderremake.common.domain.usecase.notification.update

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(notification: Notification): Observable<Result<Unit, NotYetDefinedError>> {
        return Observable.create {
            notificationRepository.update(notification)
                .run { Result.Success(Unit) }
        }
    }
}