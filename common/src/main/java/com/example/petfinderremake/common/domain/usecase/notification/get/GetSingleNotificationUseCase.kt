package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSingleNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(notificationId: Long): Flow<Result<Notification, NotYetDefinedError>> {
        return notificationRepository.getSingleNotification(notificationId)
            .map { Result.Success(it) }
    }
}