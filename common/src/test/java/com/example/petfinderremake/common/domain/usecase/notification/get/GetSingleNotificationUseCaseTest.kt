package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetSingleNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var getSingleNotificationUseCase: GetSingleNotificationUseCase

    @Before
    fun setup() {
        getSingleNotificationUseCase = GetSingleNotificationUseCase(notificationRepository)
        runBlocking {
            notificationRepository.insert(notification)
        }
    }


    @Test
    fun `when get single notification, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getSingleNotificationUseCase(notification.id).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get single notification, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getSingleNotificationUseCase(notification.id).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get single notification with previously set value, then result of type Result Success is notification of searched id`() {
        runBlocking {

            //when
            val result = getSingleNotificationUseCase(notification.id).first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(notification))

        }
    }
}