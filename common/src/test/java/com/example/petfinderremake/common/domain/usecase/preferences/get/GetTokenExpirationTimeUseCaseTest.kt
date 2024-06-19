package com.example.petfinderremake.common.domain.usecase.preferences.get

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetTokenExpirationTimeUseCaseTest : PreferencesTest() {

    private val tokenExpirationTime = 1L
    private lateinit var getTokenExpirationTimeUseCase: GetTokenExpirationTimeUseCase

    @Before
    fun setup() {
        getTokenExpirationTimeUseCase = GetTokenExpirationTimeUseCase(preferences)
        preferences.putTokenExpirationTime(tokenExpirationTime)
    }

    @Test
    fun `when get token expiration time, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Long, NotYetDefinedError>>()
        val result = getTokenExpirationTimeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get token expiration time, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Long, NotYetDefinedError>>()
        val result = getTokenExpirationTimeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get token expiration time with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<Long, NotYetDefinedError>>()
        val result = getTokenExpirationTimeUseCase().toObservable()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(tokenExpirationTime))
    }


}