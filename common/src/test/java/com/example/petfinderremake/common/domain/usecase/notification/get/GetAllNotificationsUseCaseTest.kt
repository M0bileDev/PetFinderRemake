package com.example.petfinderremake.common.domain.usecase.notification.get

import com.example.petfinderremake.NotificationRepositoryTest
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetAllNotificationsUseCaseTest : NotificationRepositoryTest() {

    private lateinit var getAllNotificationsUseCase: GetAllNotificationsUseCase

    @Before
    fun setup() {
        getAllNotificationsUseCase = GetAllNotificationsUseCase(notificationRepository)
        notificationRepository.insert(notification)
    }

    @Test
    fun `when get all notification, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getAllNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get all notification, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getAllNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get all notification with previously set value, then result of type Result Success is list of inserted notifications`() {
        val notifications = listOf(notification)

        //when
        val testObserver = TestObserver<Result<List<Notification>, NotYetDefinedError>>()
        val result = getAllNotificationsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(notifications))
    }

}