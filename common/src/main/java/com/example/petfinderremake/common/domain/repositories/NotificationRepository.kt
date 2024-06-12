package com.example.petfinderremake.common.domain.repositories

import com.example.petfinderremake.common.domain.model.notification.Notification
import io.reactivex.rxjava3.core.Observable

interface NotificationRepository {

    fun insert(notification: Notification)
    fun update(notification: Notification)
    fun delete(notification: Notification)
    fun getAllNotifications(): Observable<List<Notification>>
    fun getNotDisplayedNotifications(): Observable<List<Notification>>
    fun getSingleNotification(id: Long): Observable<Notification>

}