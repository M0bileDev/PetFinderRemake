package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetSingleNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(notificationId: Long): Observable<Result<Notification, NotYetDefinedError>> {
        return notificationRepository
            .getSingleNotification(notificationId)
            .map { Result.Success(it) }
    }
}