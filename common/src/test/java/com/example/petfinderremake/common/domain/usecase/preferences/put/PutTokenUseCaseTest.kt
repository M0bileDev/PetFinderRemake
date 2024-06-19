package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class PutTokenUseCaseTest : PreferencesTest() {

    private val token = "token"
    private lateinit var putTokenUseCase: PutTokenUseCase

    @Before
    fun setup() {
        putTokenUseCase = PutTokenUseCase(preferences)
        preferences.deleteTokenInfo()
    }

    @Test
    fun `when put token, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenUseCase(token)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when put token, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenUseCase(token)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when put token value, then result of use case is Result Success Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenUseCase(token)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when put token TOKEN, then result is TOKEN`() {

        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        putTokenUseCase(token).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<String>()
        val result = preferences.getToken().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEqualTo(token)
    }
}