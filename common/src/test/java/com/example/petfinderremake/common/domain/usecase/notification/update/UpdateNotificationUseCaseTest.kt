package com.example.petfinderremake.common.domain.usecase.notification.update

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class UpdateNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var updateNotificationUseCase: UpdateNotificationUseCase
    private val updatedNotification = notification.copy(displayed = true)

    @Before
    fun setup() {
        updateNotificationUseCase = UpdateNotificationUseCase(notificationRepository)
        notificationRepository.insert(notification)
    }

    @Test
    fun `when update notification, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = updateNotificationUseCase(updatedNotification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when update notification, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = updateNotificationUseCase(updatedNotification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when update notification, then updated value is present`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        updateNotificationUseCase(updatedNotification).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<Notification>>()
        val result = notificationRepository.getAllNotifications()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first().first()
        Truth.assertThat(sut).isEqualTo(updatedNotification)
    }
}