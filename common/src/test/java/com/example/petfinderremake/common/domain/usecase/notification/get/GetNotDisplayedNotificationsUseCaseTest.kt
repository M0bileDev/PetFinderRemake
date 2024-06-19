package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.onSuccess
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
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
        notifications.forEach {
            notificationRepository.insert(it)
        }
    }

    @Test
    fun `when get not displayed notifications, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getNotDisplayedNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get not displayed notifications, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getNotDisplayedNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get all notification with previously set value, then result of type Result Success is list of not displayed notifications only`() {
        val notDisplayedNotifications = mutableListOf<Notification>()

        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getNotDisplayedNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut =
            testObserver.values().first().onSuccess { notDisplayedNotifications.addAll(it.success) }
        Truth.assertThat(sut).isEqualTo(Result.Success(notDisplayedNotifications))
    }
}