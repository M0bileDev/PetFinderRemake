package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petfinderremake.features.notifications.presentation.navigation.NOTIFICATION_KEY
import com.example.petfinderremake.features.notifications.presentation.navigation.NotificationNavigation
import com.example.petfinderremake.features.notifications.presentation.screen.notification.NotificationFragmentDirections
import javax.inject.Inject

class PetFinderNotificationNavigation @Inject constructor() : NotificationNavigation {

    override fun navigateToNotificationDetails(fragment: Fragment, notificationId: Long) =
        with(fragment) {
            val action =
                NotificationFragmentDirections.actionNotificationFragmentToNotificationDetailsFragment(
                    notificationId
                )
            findNavController().navigate(action)
        }

    override fun getNotificationIdNavArg(fragment: Fragment): Long = with(fragment) {
        return@with arguments?.getLong(NOTIFICATION_KEY) ?: throw Exception()
    }

}