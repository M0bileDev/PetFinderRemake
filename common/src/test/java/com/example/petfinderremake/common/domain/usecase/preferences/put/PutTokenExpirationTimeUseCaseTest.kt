package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class PutTokenExpirationTimeUseCaseTest : PreferencesTest() {

    private val expirationTime = 1L
    private lateinit var putTokenExpirationTimeUseCase: PutTokenExpirationTimeUseCase

    @Before
    fun setup() {
        putTokenExpirationTimeUseCase = PutTokenExpirationTimeUseCase(preferences)
        preferences.deleteTokenInfo()
    }

    @Test
    fun `when put token expiration time, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenExpirationTimeUseCase(expirationTime)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when put token expiration time, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenExpirationTimeUseCase(expirationTime)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when put token expiration time value, then result of use case is Result Success Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenExpirationTimeUseCase(expirationTime)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when put token expiration time 1L, then result is 1L`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        putTokenExpirationTimeUseCase(expirationTime).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<Long>()
        val result = preferences.getTokenExpirationTime().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEqualTo(expirationTime)
    }

}