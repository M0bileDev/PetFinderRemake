package com.example.petfinderremake.common.domain.repositories

import com.example.petfinderremake.common.domain.model.notification.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun insert(notification: Notification)
    suspend fun update(notification: Notification)
    suspend fun delete(notification: Notification)
    fun getAllNotifications(): Flow<List<Notification>>
    fun getNotDisplayedNotifications(): Flow<List<Notification>>
    fun getSingleNotification(id: Long): Flow<Notification>

}