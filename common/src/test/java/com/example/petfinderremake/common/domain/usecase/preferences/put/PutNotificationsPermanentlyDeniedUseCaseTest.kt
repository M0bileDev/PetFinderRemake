package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PutNotificationsPermanentlyDeniedUseCaseTest : PreferencesTest() {

    private val notificationPermanentlyDenied = true
    private lateinit var putNotificationsPermanentlyDeniedUseCase: PutNotificationsPermanentlyDeniedUseCase

    @Before
    fun setup() {
        putNotificationsPermanentlyDeniedUseCase =
            PutNotificationsPermanentlyDeniedUseCase(preferences)

        runBlocking {
            preferences.putNotificationsPermanentlyDenied(false)
        }
    }

    @Test
    fun `when put notification permanently denied, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when put notification permanently denied, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when put notification permanently denied value, then result of use case is Result Success Unit`() =
        runBlocking {

            //when
            val result = putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))
        }

    @Test
    fun `when put notification permanently denied true, then result true`() =
        runBlocking {

            //when
            putNotificationsPermanentlyDeniedUseCase(notificationPermanentlyDenied)

            //then
            val result = preferences.getNotificationPermanentlyDenied().first()
            Truth.assertThat(result).isTrue()
        }
}