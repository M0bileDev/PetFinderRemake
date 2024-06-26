package com.example.petfinderremake

import com.example.petfinderremake.common.data.repositories.FakeNotificationRepository
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository

abstract class NotificationRepositoryTest {

    val notification = Notification()
    val notificationRepository: NotificationRepository = FakeNotificationRepository()
}