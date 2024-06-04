package com.example.petfinderremake.common.presentation.manager.notification

import android.content.Context

interface NotificationManager {
    fun registerPetFinderGeneralNotificationsChannel()
    fun unregisterPetFinderGeneralNotificationsChannel()
    fun showNotification(title: String, content: String)
}

