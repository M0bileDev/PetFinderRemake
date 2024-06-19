package com.example.petfinderremake.common.domain.usecase.notification.insert

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class InsertNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var insertNotificationUseCase: InsertNotificationUseCase

    @Before
    fun setup() {
        insertNotificationUseCase = InsertNotificationUseCase(notificationRepository)
        notificationRepository.delete(notification)
    }

    @Test
    fun `when insert notification, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = insertNotificationUseCase(notification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when insert notification, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = insertNotificationUseCase(notification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when insert notification, then notification is present`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        insertNotificationUseCase(notification).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<Notification>>()
        val result = notificationRepository.getAllNotifications()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isNotEmpty()
    }
}