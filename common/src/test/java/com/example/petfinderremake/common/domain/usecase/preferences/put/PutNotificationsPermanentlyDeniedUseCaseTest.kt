package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class PutNotificationsPermanentlyDeniedUseCaseTest : PreferencesTest() {

    private val notificationPermanentlyDenied = true
    private lateinit var putNotificationsPermanentlyDeniedUseCase: PutNotificationsPermanentlyDeniedUseCase

    @Before
    fun setup() {
        putNotificationsPermanentlyDeniedUseCase =
            PutNotificationsPermanentlyDeniedUseCase(preferences)
        preferences.putNotificationsPermanentlyDenied(false)
    }

    @Test
    fun `when put notification permanently denied, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when put notification permanently denied, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when put notification permanently denied value, then result of use case is Result Success Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when put notification permanently denied true, then result true`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied).subscribe(
            testObserver
        )

        //then
        val testObserver2 = TestObserver<Boolean>()
        val result = preferences.getNotificationPermanentlyDenied().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isTrue()
    }
}