package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTokenUseCaseTest : PreferencesTest() {

    private val token = "token"
    private lateinit var getTokenUseCase: GetTokenUseCase

    @Before
    fun setup() {
        getTokenUseCase = GetTokenUseCase(preferences)

        runBlocking {
            preferences.putToken(token)
        }
    }

    @Test
    fun `when get token, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = getTokenUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when get token, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = getTokenUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when get token with previously set value, then result of type Result Success is the same value`() = runBlocking {

        //when
        val result = getTokenUseCase().first()

        //then
        Truth.assertThat(result).isEqualTo(Result.Success(token))
    }
}