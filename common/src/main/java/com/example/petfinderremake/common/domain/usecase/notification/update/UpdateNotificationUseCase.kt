package com.example.petfinderremake.common.domain.usecase.notification.update

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notification: Notification): Result<Unit, NotYetDefinedError> {
        return withContext(Dispatchers.IO) {
            notificationRepository.update(notification)
                .run { Result.Success(Unit) }
        }
    }
}