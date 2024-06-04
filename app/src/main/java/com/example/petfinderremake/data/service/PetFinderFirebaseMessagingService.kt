package com.example.petfinderremake.data.service

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.usecase.notification.insert.InsertNotificationUseCase
import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.logging.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class PetFinderFirebaseMessagingService : FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var insertNotificationUseCase: InsertNotificationUseCase

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        Logger.d("$TAG refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        Logger.d("$TAG onMessageReceived: $message")

        val createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val data = message.data
        val title = data["title"].orEmpty()
        val description = data["body"].orEmpty()

        scope.launch {
            insertNotificationUseCase(
                Notification(
                    title = title,
                    description = description,
                    createdAt = createdAt
                )
            )
        }

        notificationManager.showNotification(title, description)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val TAG = "PetFinderFirebaseMessagingService"
    }
}