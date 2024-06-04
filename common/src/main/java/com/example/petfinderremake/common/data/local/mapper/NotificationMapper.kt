package com.example.petfinderremake.common.data.local.mapper

import com.example.petfinderremake.common.data.local.model.NotificationEntity
import com.example.petfinderremake.common.domain.model.notification.Notification
import javax.inject.Inject

class NotificationMapper @Inject constructor() : DatabaseMapper<NotificationEntity, Notification> {

    override fun mapToDomain(databaseEntity: NotificationEntity): Notification =
        with(databaseEntity) {
            return Notification(
                id, title, description, createdAt, displayed
            )
        }

    override fun mapToDatabaseEntity(domain: Notification): NotificationEntity = with(domain) {
        return NotificationEntity(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt,
            displayed = displayed
        )
    }

}