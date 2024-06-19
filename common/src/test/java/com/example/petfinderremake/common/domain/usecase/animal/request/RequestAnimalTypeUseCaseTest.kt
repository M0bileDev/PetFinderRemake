package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.onError
import com.google.common.truth.Truth
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class RequestAnimalTypeUseCaseTest : AnimalRepositoryTest() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockAnimalRepository: AnimalRepository

    @MockK
    lateinit var httpException: HttpException

    @MockK(relaxed = true)
    private var mockOnLoading: (Boolean) -> Unit = {}

    private val type = "type"
    private val onLoading: (Boolean) -> Unit = {}

    private lateinit var requestAnimalTypeUseCase: RequestAnimalTypeUseCase
    private lateinit var mockRequestAnimalTypeUseCase: RequestAnimalTypeUseCase
    private val animalType = AnimalType(emptyList(), emptyList(), emptyList(), "Type1")
    private val storeAnimalType = listOf(animalType)

    @Before
    fun setup() {
        requestAnimalTypeUseCase = RequestAnimalTypeUseCase(animalRepository)
        mockRequestAnimalTypeUseCase = RequestAnimalTypeUseCase(mockAnimalRepository)
    }

    @Test
    fun `when request animal single type, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalTypeUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when request animal single type without any exceptions, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalTypeUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when request animal single type without any exceptions, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalTypeUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when request animal single type with network exceptions, then result of use case is instance of Result Error type`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request animal single type with network exceptions, then result of Error is type of NetworkError `() {
        var sut: RootError? = null

        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(NetworkError::class.java)

    }

    @Test
    fun `when request animal single type with network exceptions, then onLoading returns false `() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestAnimalTypeUseCase(type, mockOnLoading).subscribe(testObserver)

        //then
        verifyOrder {
            mockOnLoading(true)
            mockOnLoading(false)
        }
    }

    @Test
    fun `when request animal single type with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))
    }

    @Test
    fun `when request animal single type with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))
    }

    @Test
    fun `when request animal single type with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        //when
        every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.assertValue(Result.Error(NetworkError.NOT_FOUND))
    }

    @Test
    fun `when request animal single type with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        //when
        every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.assertValue(Result.Error(NetworkError.UNEXPECTED_ERROR))
    }

    @Test
    fun `when request animal single type with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        //when
        every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.assertValue(Result.Error(NetworkError.MISSING_PARAMETERS))
    }

    @Test
    fun `when request animal single type with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        //when
        every { httpException.code() }.returns(0)
        every { mockAnimalRepository.requestAnimalType(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver
            .assertNoValues()
            .assertNotComplete()
            .onError(
                NetworkError.NetworkErrorTypeException()
            )
    }

    @Test
    fun `when request animal single type with empty argument exceptions, then result of use case is instance of Result Error type`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase("", mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request animal single type with empty argument exception, then result of Error is type of ArgumentError`() {
        var sut: RootError? = null

        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase("", mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(ArgumentError::class.java)
    }

    @Test
    fun `when request animal single type with empty argument exception, then ArgumentError is ARGUMENT_IS_EMPTY`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalTypeUseCase("", mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY))
    }

    @Test
    fun `when request animal single type with empty argument exception, then onLoading returns false was never called`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestAnimalTypeUseCase("", mockOnLoading).subscribe(testObserver)

        //then
        verify {
            mockOnLoading wasNot Called
        }
    }

    @Test
    fun `when request animal single type without exceptions, then store animal types and loading are executed in order was not called`() {
        //when
        every { mockAnimalRepository.requestAnimalType(type) }.returns(Observable.just(animalType))
        every { mockAnimalRepository.storeAnimalTypes(storeAnimalType) }.returns(Completable.complete())

        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestAnimalTypeUseCase(type, mockOnLoading).subscribe(testObserver)


        //then
        verifyOrder {
            mockOnLoading(true)
            mockAnimalRepository.storeAnimalTypes(storeAnimalType)
            mockOnLoading(false)
        }
    }

    @Test
    fun `when request animal single type, then single type is stored`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        requestAnimalTypeUseCase(type, onLoading).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<AnimalType>>()
        val result = animalRepository.getAnimalTypes()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isNotEmpty()
    }
}