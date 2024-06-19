package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetNotificationPermanentlyDeniedUseCaseTest : PreferencesTest() {

    private val notificationPermanentlyDenied = true
    private lateinit var getNotificationPermanentlyDeniedUseCase: GetNotificationPermanentlyDeniedUseCase

    @Before
    fun setup() {
        getNotificationPermanentlyDeniedUseCase =
            GetNotificationPermanentlyDeniedUseCase(preferences)
        preferences.putNotificationsPermanentlyDenied(notificationPermanentlyDenied)
    }

    @Test
    fun `when get notification permanently denied, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Boolean, NotYetDefinedError>>()
        val result = getNotificationPermanentlyDeniedUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get notification permanently denied, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Boolean, NotYetDefinedError>>()
        val result = getNotificationPermanentlyDeniedUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get notification permanently denied with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<Boolean, NotYetDefinedError>>()
        val result = getNotificationPermanentlyDeniedUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(Result.Success(notificationPermanentlyDenied))
    }

}