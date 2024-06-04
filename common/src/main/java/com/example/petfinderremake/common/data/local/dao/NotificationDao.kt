package com.example.petfinderremake.common.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petfinderremake.common.data.local.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert
    suspend fun insert(notificationEntity: NotificationEntity)

    @Delete
    suspend fun delete(notificationEntity: NotificationEntity)

    @Update
    suspend fun update(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM notifications")
    fun getAllNotification(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE displayed == 0")
    fun getNotDisplayedNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id =:notificationId ")
    fun getSingleNotification(notificationId: Long): Flow<NotificationEntity>

}