package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetSingleNotificationUseCaseTest : NotificationRepositoryTest() {

    private lateinit var getSingleNotificationUseCase: GetSingleNotificationUseCase

    @Before
    fun setup() {
        getSingleNotificationUseCase = GetSingleNotificationUseCase(notificationRepository)
        notificationRepository.insert(notification)
    }


    @Test
    fun `when get single notification, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Notification, NotYetDefinedError>>()
        val result = getSingleNotificationUseCase(notification.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get single notification, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Notification, NotYetDefinedError>>()
        val result = getSingleNotificationUseCase(notification.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get single notification with previously set value, then result of type Result Success is notification of searched id`() {
        //when
        val testObserver = TestObserver<Result<Notification, NotYetDefinedError>>()
        val result = getSingleNotificationUseCase(notification.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(notification))
    }
}