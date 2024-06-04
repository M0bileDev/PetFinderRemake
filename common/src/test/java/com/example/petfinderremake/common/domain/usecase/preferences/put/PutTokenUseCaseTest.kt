package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PutTokenUseCaseTest : PreferencesTest() {

    private val token = "token"
    private lateinit var putTokenUseCase: PutTokenUseCase

    @Before
    fun setup() {

        putTokenUseCase = PutTokenUseCase(preferences)

        runBlocking {
            preferences.deleteTokenInfo()
        }
    }

    @Test
    fun `when put token, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = putTokenUseCase(token)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when put token, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = putTokenUseCase(token)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when put token value, then result of use case is Result Success Unit`() =
        runBlocking {

            //when
            val result = putTokenUseCase(token)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))
        }

    @Test
    fun `when put token TOKEN, then result is TOKEN`() =
        runBlocking {

            //when
            putTokenUseCase(token)

            //then
            val result = preferences.getToken().first()
            Truth.assertThat(result).isEqualTo(token)
        }
}