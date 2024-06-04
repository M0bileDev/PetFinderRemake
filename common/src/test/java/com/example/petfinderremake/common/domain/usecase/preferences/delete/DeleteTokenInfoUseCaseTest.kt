package com.example.petfinderremake.common.domain.usecase.preferences.delete

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteTokenInfoUseCaseTest : PreferencesTest() {

    private val token = "token"
    private val tokenType = "type"
    private val tokenExpirationTime = 1L

    private lateinit var deleteTokenInfoUseCase: DeleteTokenInfoUseCase

    @Before
    fun setup() {
        deleteTokenInfoUseCase = DeleteTokenInfoUseCase(preferences)

        runBlocking {
            with(preferences) {
                launch {
                    putToken(token)
                }
                launch {
                    putTokenType(tokenType)
                }
                launch {
                    putTokenExpirationTime(tokenExpirationTime)
                }
            }

        }
    }

    @Test
    fun `when delete token info, then token info (token, type and expiration time) is default`() {
        runBlocking {

            //when
            deleteTokenInfoUseCase()

            //then
            val type = preferences.getTokenType().first()
            val token = preferences.getToken().first()
            val tokenExpirationTime = preferences.getTokenExpirationTime().first()

            assertThat(type).isEmpty()
            assertThat(token).isEmpty()
            assertThat(tokenExpirationTime).isEqualTo(-1L)
        }
    }

    @Test
    fun `when delete token info, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = deleteTokenInfoUseCase()

            //then
            assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when delete token info, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = deleteTokenInfoUseCase()

            //then
            assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when delete token info, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = deleteTokenInfoUseCase()

            //then
            assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }

}