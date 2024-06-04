package com.example.petfinderremake.features.notifications.presentation.screen.notification

import com.example.petfinderremake.common.domain.model.notification.Notification

data class NotificationUiState(
    val granted: Boolean = false,
    val denied: Boolean = false,
    val permanentlyDenied: Boolean = false,
    val notifications: List<Notification> = emptyList()
) {
    val notGranted = denied || permanentlyDenied
}