package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetTokenTypeUseCaseTest : PreferencesTest() {

    private val tokenType = "tokenType"
    private lateinit var getTokenTypeUseCase: GetTokenTypeUseCase

    @Before
    fun setup() {
        getTokenTypeUseCase = GetTokenTypeUseCase(preferences)
        preferences.putTokenType(tokenType)
    }

    @Test
    fun `when get token type, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenTypeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get token type, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenTypeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get token type with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenTypeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(tokenType))
    }

}