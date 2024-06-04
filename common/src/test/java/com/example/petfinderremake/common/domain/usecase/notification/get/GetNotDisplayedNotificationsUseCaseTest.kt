package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.onSuccess
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotDisplayedNotificationsUseCaseTest : NotificationRepositoryTest() {

    private lateinit var getNotDisplayedNotificationsUseCase: GetNotDisplayedNotificationsUseCase
    private val notifications = listOf(
        notification.copy(id = 1),
        notification.copy(id = 2, displayed = true),
        notification.copy(id = 3)
    )

    @Before
    fun setup() {
        getNotDisplayedNotificationsUseCase =
            GetNotDisplayedNotificationsUseCase(notificationRepository)

        runBlocking {
            notifications.forEach {
                notificationRepository.insert(it)
            }
        }
    }

    @Test
    fun `when get not displayed notifications, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getNotDisplayedNotificationsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get not displayed notifications, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getNotDisplayedNotificationsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get all notification with previously set value, then result of type Result Success is list of not displayed notifications only`() {
        runBlocking {

            //when
            val notDisplayedNotifications = mutableListOf<Notification>()
            val result = getNotDisplayedNotificationsUseCase().first()
                .onSuccess { notDisplayedNotifications.addAll(it.success) }

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(notDisplayedNotifications))

        }
    }
}