package com.example.petfinderremake.features.notifications.presentation.navigation

import androidx.fragment.app.Fragment

interface NotificationNavigation {
    fun navigateToNotificationDetails(fragment: Fragment, notificationId: Long)
    fun getNotificationIdNavArg(fragment: Fragment) : Long
}