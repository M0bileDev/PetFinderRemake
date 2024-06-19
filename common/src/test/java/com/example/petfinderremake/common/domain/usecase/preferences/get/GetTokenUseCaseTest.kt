package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetTokenUseCaseTest : PreferencesTest() {

    private val token = "token"
    private lateinit var getTokenUseCase: GetTokenUseCase

    @Before
    fun setup() {
        getTokenUseCase = GetTokenUseCase(preferences)
        preferences.putToken(token)
    }

    @Test
    fun `when get token, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get token, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get token with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<String, NotYetDefinedError>>()
        val result = getTokenUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(token))
    }
}