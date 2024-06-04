package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PutTokenTypeUseCaseTest : PreferencesTest(){
    
    private val type = "type"
    private lateinit var putTokenTypeUseCase: PutTokenTypeUseCase
    
    @Before
    fun setup(){

        putTokenTypeUseCase = PutTokenTypeUseCase(preferences)
        
        runBlocking { 
            preferences.deleteTokenInfo()
        }
    }

    @Test
    fun `when put token type, then result of use case is instance of Result`() =
        runBlocking {

            //when
            val result = putTokenTypeUseCase(type)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)
        }

    @Test
    fun `when put token type, then result of use case is instance of Result Success`() =
        runBlocking {

            //when
            val result = putTokenTypeUseCase(type)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when put token type value, then result of use case is Result Success Unit`() =
        runBlocking {

            //when
            val result = putTokenTypeUseCase(type)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))
        }

    @Test
    fun `when put token type TYPE, then result is TYPE`() =
        runBlocking {

            //when
            putTokenTypeUseCase(type)

            //then
            val result = preferences.getTokenType().first()
            Truth.assertThat(result).isEqualTo(type)
        }
}