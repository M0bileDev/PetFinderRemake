package com.example.petfinderremake.common.domain.usecase.notification.delete

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DeleteNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var deleteNotificationUseCase: DeleteNotificationUseCase

    @Before
    fun setup() {
        deleteNotificationUseCase = DeleteNotificationUseCase(notificationRepository)
        notificationRepository.insert(notification)
    }

    @Test
    fun `when delete notification, then notification is not present`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteNotificationUseCase(notification).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<Notification>>()
        val result = notificationRepository.getAllNotifications()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEmpty()
    }

    @Test
    fun `when delete notification, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteNotificationUseCase(notification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when delete notification, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteNotificationUseCase(notification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when delete notification, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteNotificationUseCase(notification)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }


}