package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.data.local.dao.NotificationDao
import com.example.petfinderremake.common.data.local.mapper.NotificationMapper
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetFinderNotificationRepository @Inject constructor(
    private val notificationMapper: NotificationMapper,
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override suspend fun insert(notification: Notification) {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        notificationDao.insert(notificationEntity)
    }

    override suspend fun update(notification: Notification) {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        notificationDao.update(notificationEntity)
    }

    override suspend fun delete(notification: Notification) {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        notificationDao.delete(notificationEntity)
    }

    override fun getAllNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotification()
            .map { entities -> entities.map { entity -> notificationMapper.mapToDomain(entity) } }

    }

    override fun getNotDisplayedNotifications(): Flow<List<Notification>> {
        return notificationDao.getNotDisplayedNotifications()
            .map { entities -> entities.map { entity -> notificationMapper.mapToDomain(entity) } }
    }

    override fun getSingleNotification(id: Long): Flow<Notification> {
        return notificationDao.getSingleNotification(id)
            .map { entity -> notificationMapper.mapToDomain(entity) }
    }
}