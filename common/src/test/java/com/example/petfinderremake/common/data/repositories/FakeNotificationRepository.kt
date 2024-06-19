package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class FakeNotificationRepository : NotificationRepository {

    private val notifications = mutableListOf<Notification>()

    override
    fun insert(notification: Notification): Completable {
        notifications.add(notification)
        return Completable.complete()
    }

    override
    fun update(notification: Notification): Completable {
        val notificationToUpdate = notifications.first { it.id == notification.id }
        val index = notifications.indexOf(notificationToUpdate)
        notifications[index] = notification
        return Completable.complete()
    }

    override
    fun delete(notification: Notification): Completable {
        notifications.remove(notification)
        return Completable.complete()
    }

    override fun getAllNotifications(): Observable<List<Notification>> {
        return Observable.just(notifications)
    }

    override fun getNotDisplayedNotifications(): Observable<List<Notification>> {
        return Observable.just(notifications).map { it.filter { !it.displayed } }
    }

    override fun getSingleNotification(id: Long): Observable<Notification> {
        return Observable.just(notifications).map { it.first { it.id == id } }
    }

}