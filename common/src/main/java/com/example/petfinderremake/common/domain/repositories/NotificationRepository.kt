package com.example.petfinderremake.common.domain.repositories

import com.example.petfinderremake.common.domain.model.notification.Notification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface NotificationRepository {

    fun insert(notification: Notification):Completable
    fun update(notification: Notification):Completable
    fun delete(notification: Notification):Completable
    fun getAllNotifications(): Observable<List<Notification>>
    fun getNotDisplayedNotifications(): Observable<List<Notification>>
    fun getSingleNotification(id: Long): Observable<Notification>

}