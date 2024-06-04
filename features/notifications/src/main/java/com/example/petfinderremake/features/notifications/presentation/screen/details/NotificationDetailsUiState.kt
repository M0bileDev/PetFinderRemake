package com.example.petfinderremake.features.notifications.presentation.screen.details

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.presentation.utils.DateFormatUtils
import java.time.LocalDateTime
import java.time.ZoneOffset

data class NotificationDetailsUiState(
    private val notification: Notification = Notification()
) {

    companion object {
        val noNotification = Notification()
    }

    private val localDateTime =
        LocalDateTime.ofEpochSecond(notification.createdAt, 0, ZoneOffset.UTC)

    val createdAt = DateFormatUtils.format(localDateTime)
    val title = notification.title
    val description = notification.description
}