package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTokenExpirationTimeUseCaseTest : PreferencesTest() {

    private val tokenExpirationTime = 1L
    private lateinit var getTokenExpirationTimeUseCase: GetTokenExpirationTimeUseCase

    @Before
    fun setup() {

        getTokenExpirationTimeUseCase = GetTokenExpirationTimeUseCase(preferences)

        runBlocking {
            preferences.putTokenExpirationTime(tokenExpirationTime)
        }
    }

    @Test
    fun `when get token expiration time, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = getTokenExpirationTimeUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when get token expiration time, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = getTokenExpirationTimeUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when get token expiration time with previously set value, then result of type Result Success is the same value`() = runBlocking {

        //when
        val result = getTokenExpirationTimeUseCase().first()

        //then
        Truth.assertThat(result).isEqualTo(Result.Success(tokenExpirationTime))
    }


}