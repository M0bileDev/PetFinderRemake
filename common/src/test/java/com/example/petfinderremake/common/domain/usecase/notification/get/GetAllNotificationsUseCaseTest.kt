package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllNotificationsUseCaseTest : NotificationRepositoryTest() {

    private lateinit var getAllNotificationsUseCase: GetAllNotificationsUseCase

    @Before
    fun setup() {
        getAllNotificationsUseCase = GetAllNotificationsUseCase(notificationRepository)

        runBlocking {
            notificationRepository.insert(notification)
        }
    }

    @Test
    fun `when get all notification, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getAllNotificationsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get all notification, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getAllNotificationsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get all notification with previously set value, then result of type Result Success is list of inserted notifications`() {
        runBlocking {

            //when
            val result = getAllNotificationsUseCase().first()

            //then
            val notifications = listOf(notification)
            Truth.assertThat(result).isEqualTo(Result.Success(notifications))

        }
    }

}