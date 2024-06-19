package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.data.local.dao.NotificationDao
import com.example.petfinderremake.common.data.local.mapper.NotificationMapper
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PetFinderNotificationRepository @Inject constructor(
    private val notificationMapper: NotificationMapper,
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun insert(notification: Notification): Completable {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        return notificationDao.insert(notificationEntity)
    }

    override fun update(notification: Notification): Completable {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        return notificationDao.update(notificationEntity)
    }

    override fun delete(notification: Notification): Completable {
        val notificationEntity = notificationMapper.mapToDatabaseEntity(notification)
        return notificationDao.delete(notificationEntity)
    }

    override fun getAllNotifications(): Observable<List<Notification>> {
        return notificationDao.getAllNotification()
            .map { entities -> entities.map { entity -> notificationMapper.mapToDomain(entity) } }

    }

    override fun getNotDisplayedNotifications(): Observable<List<Notification>> {
        return notificationDao.getNotDisplayedNotifications()
            .map { entities -> entities.map { entity -> notificationMapper.mapToDomain(entity) } }
    }

    override fun getSingleNotification(id: Long): Observable<Notification> {
        return notificationDao.getSingleNotification(id)
            .map { entity -> notificationMapper.mapToDomain(entity) }
    }
}