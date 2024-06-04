package com.example.petfinderremake.common.domain.usecase.notification.update

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var updateNotificationUseCase: UpdateNotificationUseCase
    private val updatedNotification = notification.copy(displayed = true)

    @Before
    fun setup() {
        updateNotificationUseCase = UpdateNotificationUseCase(notificationRepository)

        runBlocking {
            notificationRepository.insert(notification)
        }
    }

    @Test
    fun `when update notification, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = updateNotificationUseCase(updatedNotification)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when update notification, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = updateNotificationUseCase(updatedNotification)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when update notification, then updated value is present`() {
        runBlocking {

            //when
            updateNotificationUseCase(updatedNotification)

            //then
            val result = notificationRepository.getAllNotifications().first().first()
            Truth.assertThat(result).isEqualTo(updatedNotification)

        }
    }
}