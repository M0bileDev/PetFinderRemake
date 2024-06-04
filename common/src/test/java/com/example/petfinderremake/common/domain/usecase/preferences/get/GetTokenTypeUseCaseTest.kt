package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTokenTypeUseCaseTest : PreferencesTest() {

    private val tokenType = "token"
    private lateinit var getTokenTypeUseCase: GetTokenTypeUseCase

    @Before
    fun setup() {
        getTokenTypeUseCase = GetTokenTypeUseCase(preferences)

        runBlocking {
            preferences.putTokenType(tokenType)
        }
    }

    @Test
    fun `when get token type, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = getTokenTypeUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when get token type, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = getTokenTypeUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when get token type with previously set value, then result of type Result Success is the same value`() = runBlocking {

        //when
        val result = getTokenTypeUseCase().first()

        //then
        Truth.assertThat(result).isEqualTo(Result.Success(tokenType))
    }

}