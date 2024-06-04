package com.example.petfinderremake.presentation.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.common.presentation.manager.notification.PETFINDER_GENERAL_NOTIFICATIONS_CHANNEL
import com.example.petfinderremake.common.presentation.utils.commonDrawable
import com.example.petfinderremake.common.presentation.utils.commonString
import com.example.petfinderremake.presentation.activity.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PetFinderNotificationManager @Inject constructor() : NotificationManager {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun registerPetFinderGeneralNotificationsChannel() = with(context) {
        val name = getString(commonString.notification_channel_name)
        val descriptionText = getString(commonString.notification_channel_description)
        val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(
                PETFINDER_GENERAL_NOTIFICATIONS_CHANNEL,
                name,
                importance
            ).apply {
                description = descriptionText
            }
        // Register the channel with the system
        val notificationManager: android.app.NotificationManager =
            getSystemService(android.app.NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun unregisterPetFinderGeneralNotificationsChannel() = with(context) {
        val notificationManager: android.app.NotificationManager =
            getSystemService(android.app.NotificationManager::class.java)
        notificationManager.deleteNotificationChannel(PETFINDER_GENERAL_NOTIFICATIONS_CHANNEL)
    }

    @SuppressLint("MissingPermission")
    override fun showNotification(title: String, content: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, PETFINDER_GENERAL_NOTIFICATIONS_CHANNEL)
            .setSmallIcon(commonDrawable.ic_notification) // replace with your own icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }

    }


}