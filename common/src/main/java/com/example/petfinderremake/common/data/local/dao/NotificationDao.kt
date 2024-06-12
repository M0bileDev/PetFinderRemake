package com.example.petfinderremake.common.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petfinderremake.common.data.local.model.NotificationEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface NotificationDao {

    @Insert
    fun insert(notificationEntity: NotificationEntity): Completable

    @Delete
    fun delete(notificationEntity: NotificationEntity) : Completable

    @Update
    fun update(notificationEntity: NotificationEntity): Completable

    @Query("SELECT * FROM notifications")
    fun getAllNotification(): Observable<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE displayed == 0")
    fun getNotDisplayedNotifications(): Observable<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id =:notificationId ")
    fun getSingleNotification(notificationId: Long): Observable<NotificationEntity>

}