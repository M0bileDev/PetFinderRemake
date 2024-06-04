package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNotificationRepository : NotificationRepository {

    private val notifications = mutableListOf<Notification>()

    override suspend fun insert(notification: Notification) {
        notifications.add(notification)
    }

    override suspend fun update(notification: Notification) {
        val notificationToUpdate = notifications.first { it.id == notification.id }
        val index = notifications.indexOf(notificationToUpdate)
        notifications[index] = notification
    }

    override suspend fun delete(notification: Notification) {
        notifications.remove(notification)
    }

    override fun getAllNotifications(): Flow<List<Notification>> {
        return flow { emit(notifications) }
    }

    override fun getNotDisplayedNotifications(): Flow<List<Notification>> {
        return flow { emit(notifications.filter { !it.displayed }) }
    }

    override fun getSingleNotification(id: Long): Flow<Notification> {
        return flow { emit(notifications.first { it.id == id }) }
    }

}