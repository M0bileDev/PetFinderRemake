package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PutTokenExpirationTimeUseCaseTest : PreferencesTest() {

    private val expirationTime = 1L
    private lateinit var putTokenExpirationTimeUseCase: PutTokenExpirationTimeUseCase

    @Before
    fun setup() {
        putTokenExpirationTimeUseCase = PutTokenExpirationTimeUseCase(preferences)

        runBlocking {
            preferences.deleteTokenInfo()
        }
    }

    @Test
    fun `when put token expiration time, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = putTokenExpirationTimeUseCase(expirationTime)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when put token expiration time, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = putTokenExpirationTimeUseCase(expirationTime)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when put token expiration time value, then result of use case is Result Success Unit`() =
        runBlocking {

            //when
            val result = putTokenExpirationTimeUseCase(expirationTime)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))
        }

    @Test
    fun `when put token expiration time 1L, then result is 1L`() =
        runBlocking {

            //when
            putTokenExpirationTimeUseCase(expirationTime)

            //then
            val result = preferences.getTokenExpirationTime().first()
            Truth.assertThat(result).isEqualTo(expirationTime)
        }

}