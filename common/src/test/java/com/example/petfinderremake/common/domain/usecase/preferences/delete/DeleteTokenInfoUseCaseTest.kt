package com.example.petfinderremake.common.domain.usecase.preferences.delete

import com.example.petfinderremake.PreferencesTest
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.observers.TestObserver
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
        with(preferences) {
            putToken(token)
            putTokenType(tokenType)
            putTokenExpirationTime(tokenExpirationTime)
        }
    }

    @Test
    fun `when delete token info, then token type is empty`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteTokenInfoUseCase().subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<String>()
        val result = preferences.getTokenType().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        assertThat(sut).isEmpty()
    }

    @Test
    fun `when delete token info, then token is empty`() {


        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteTokenInfoUseCase().subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<String>()
        val result = preferences.getToken().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        assertThat(sut).isEmpty()


    }

    @Test
    fun `when delete token info, then expiration time is -1L`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteTokenInfoUseCase().subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<Long>()
        val result = preferences.getTokenExpirationTime().toObservable()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        assertThat(sut).isEqualTo(-1L)
    }

    @Test
    fun `when delete token info, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteTokenInfoUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when delete token info, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteTokenInfoUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isInstanceOf(Result.Success::class.java)


    }

    @Test
    fun `when delete token info, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteTokenInfoUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        assertThat(sut).isEqualTo(Result.Success(Unit))
    }

}