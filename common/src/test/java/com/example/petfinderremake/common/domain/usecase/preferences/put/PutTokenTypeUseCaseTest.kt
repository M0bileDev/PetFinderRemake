package com.example.petfinderremake.common.domain.usecase.preferences.put

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class PutTokenTypeUseCaseTest : PreferencesTest() {

    private val type = "type"
    private lateinit var putTokenTypeUseCase: PutTokenTypeUseCase

    @Before
    fun setup() {
        putTokenTypeUseCase = PutTokenTypeUseCase(preferences)
        preferences.deleteTokenInfo()
    }

    @Test
    fun `when put token type, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when put token type, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when put token type value, then result of use case is Result Success Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = putTokenTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when put token type TYPE, then result is TYPE`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        putTokenTypeUseCase(type).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<String>()
        val result = preferences.getTokenType().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEqualTo(type)
    }
}