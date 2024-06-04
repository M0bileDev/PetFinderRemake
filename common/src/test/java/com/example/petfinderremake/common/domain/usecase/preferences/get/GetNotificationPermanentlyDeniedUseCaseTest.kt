package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotificationPermanentlyDeniedUseCaseTest : PreferencesTest() {

    private val notificationPermanentlyDenied = true
    private lateinit var getNotificationPermanentlyDeniedUseCase: GetNotificationPermanentlyDeniedUseCase

    @Before
    fun setup() {
        getNotificationPermanentlyDeniedUseCase =
            GetNotificationPermanentlyDeniedUseCase(preferences)

        runBlocking {
            preferences.putNotificationsPermanentlyDenied(notificationPermanentlyDenied)
        }
    }

    @Test
    fun `when get notification permanently denied, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = getNotificationPermanentlyDeniedUseCase().first()

            //then
            assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when get notification permanently denied, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = getNotificationPermanentlyDeniedUseCase().first()

            //then
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when get notification permanently denied with previously set value, then result of type Result Success is the same value`() =
        runBlocking {

            //when
            val result = getNotificationPermanentlyDeniedUseCase().first()

            //then
            assertThat(result).isEqualTo(Result.Success(notificationPermanentlyDenied))
        }

}