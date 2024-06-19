package com.example.petfinderremake.common.domain.usecase.notification.update

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(notification: Notification): Observable<Result<Unit, NotYetDefinedError>> {
        return notificationRepository
            .update(notification)
            .subscribeOn(Schedulers.io())
            .toSingleDefault(Unit)
            .toObservable()
            .map { Result.Success(Unit) }
    }
}