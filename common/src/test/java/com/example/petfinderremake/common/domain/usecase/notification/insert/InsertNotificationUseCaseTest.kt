package com.example.petfinderremake.common.domain.usecase.notification.insert

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var insertNotificationUseCase: InsertNotificationUseCase

    @Before
    fun setup() {
        insertNotificationUseCase = InsertNotificationUseCase(notificationRepository)

        runBlocking {
            notificationRepository.delete(notification)
        }
    }

    @Test
    fun `when insert notification, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = insertNotificationUseCase(notification)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when insert notification, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = insertNotificationUseCase(notification)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when insert notification, then notification is present`() {
        runBlocking {

            //when
            insertNotificationUseCase(notification)

            //then
            val notifications = notificationRepository.getAllNotifications().first()
            Truth.assertThat(notifications).isNotEmpty()

        }
    }
}