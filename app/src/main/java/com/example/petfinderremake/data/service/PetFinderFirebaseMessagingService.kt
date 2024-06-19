package com.example.petfinderremake.data.service

import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.usecase.notification.insert.InsertNotificationUseCase
import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.logging.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class PetFinderFirebaseMessagingService : FirebaseMessagingService() {

    private val subscriptions = CompositeDisposable()

    @Inject
    lateinit var insertNotificationUseCase: InsertNotificationUseCase

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        Logger.d("$TAG refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val data = message.data
        val title = data["title"].orEmpty()
        val description = data["body"].orEmpty()


        insertNotificationUseCase(
            Notification(
                title = title,
                description = description,
                createdAt = createdAt
            )
        ).subscribe()
            .addTo(subscriptions)


        notificationManager.showNotification(title, description)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    companion object {
        private const val TAG = "PetFinderFirebaseMessagingService"
    }
}