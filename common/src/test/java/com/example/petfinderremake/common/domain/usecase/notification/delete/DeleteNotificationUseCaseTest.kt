package com.example.petfinderremake.common.domain.usecase.notification.delete

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var deleteNotificationUseCase: DeleteNotificationUseCase

    @Before
    fun setup() {
        deleteNotificationUseCase = DeleteNotificationUseCase(notificationRepository)

        runBlocking {
            notificationRepository.insert(notification)
        }
    }

    @Test
    fun `when delete notification, then notification is not present`() {
        runBlocking {

            //when
            deleteNotificationUseCase(notification)

            //then
            val notifications = notificationRepository.getAllNotifications().first()
            Truth.assertThat(notifications).isEmpty()

        }
    }

    @Test
    fun `when delete notification, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = deleteNotificationUseCase(notification)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when delete notification, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = deleteNotificationUseCase(notification)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when delete notification, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = deleteNotificationUseCase(notification)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }


}