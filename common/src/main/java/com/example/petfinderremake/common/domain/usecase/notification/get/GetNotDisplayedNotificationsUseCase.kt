package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetNotDisplayedNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Observable<Result<List<Notification>, NotYetDefinedError>> {
        return notificationRepository
            .getNotDisplayedNotifications()
            .subscribeOn(Schedulers.io())
            .map { Result.Success(it) }
    }
}