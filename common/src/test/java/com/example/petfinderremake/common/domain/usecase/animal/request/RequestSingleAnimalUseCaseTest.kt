package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
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

class RequestSingleAnimalUseCaseTest : AnimalRepositoryTest() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockAnimalRepository: AnimalRepository

    @MockK
    lateinit var httpException: HttpException

    @MockK(relaxed = true)
    private var mockOnLoading: (Boolean) -> Unit = {}

    private val id = 0L
    private val negativeId = -1L
    private val onLoading: (Boolean) -> Unit = {}

    private lateinit var requestSingleAnimalUseCase: RequestSingleAnimalUseCase
    private lateinit var mockRequestSingleAnimalUseCase: RequestSingleAnimalUseCase
    private val animalWithDetails = AnimalWithDetails.noAnimalWithDetails
    private val storeAnimal = listOf(animalWithDetails)

    @Before
    fun setup() {
        requestSingleAnimalUseCase = RequestSingleAnimalUseCase(animalRepository)
        mockRequestSingleAnimalUseCase = RequestSingleAnimalUseCase(mockAnimalRepository)
    }

    @Test
    fun `when request single animal , then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestSingleAnimalUseCase(id, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when request single animal  without any exceptions, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestSingleAnimalUseCase(id, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when request single animal  without any exceptions, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestSingleAnimalUseCase(id, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when request single animal  with network exceptions, then result of use case is instance of Result Error type`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request single animal  with network exceptions, then result of Error is type of NetworkError `() {
        var sut: RootError? = null

        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(NetworkError::class.java)
    }

    @Test
    fun `when request single animal  with network exceptions, then onLoading returns false `() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestSingleAnimalUseCase(id, mockOnLoading).subscribe(testObserver)

        //then
        verifyOrder {
            mockOnLoading(true)
            mockOnLoading(false)
        }
    }

    @Test
    fun `when request single animal  with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))
    }

    @Test
    fun `when request single animal  with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))
    }

    @Test
    fun `when request single animal  with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        //when
        every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.NOT_FOUND))
    }

    @Test
    fun `when request single animal  with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        //when
        every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.UNEXPECTED_ERROR))
    }

    @Test
    fun `when request single animal  with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        //when
        every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.MISSING_PARAMETERS))
    }

    @Test
    fun `when request single animal  with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        //when
        every { httpException.code() }.returns(0)
        every { mockAnimalRepository.requestAnimal(id) }.returns(
            Observable.error(
                httpException
            )
        )

        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(id, mockOnLoading)
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
    fun `when request single animal with negative numeric argument exception, then result of use case is instance of Result Error type`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(negativeId, mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then result of Error is type of ArgumentError`() {
        var sut: RootError? = null

        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(negativeId, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(ArgumentError::class.java)
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then ArgumentError is ARGUMENT_IS_EMPTY`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestSingleAnimalUseCase(negativeId, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(ArgumentError.ARGUMENT_IS_NEGATIVE))
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then onLoading was not called `() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestSingleAnimalUseCase(negativeId, mockOnLoading).subscribe(testObserver)

        //then
        verify { mockOnLoading wasNot Called }
    }

    @Test
    fun `when request single animal without exceptions, then store animals and loading are executed in order`() {
        //when
        every { mockAnimalRepository.requestAnimal(id) }.returns(Observable.just(animalWithDetails))
        every { mockAnimalRepository.storeAnimals(storeAnimal) }.returns(Completable.complete())

        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestSingleAnimalUseCase(id, mockOnLoading).subscribe(testObserver)


        //then
        verifyOrder {
            mockAnimalRepository.storeAnimals(storeAnimal)
            mockOnLoading(false)
        }
    }

    @Test()
    fun `when request single animal, then animal is stored`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        requestSingleAnimalUseCase(id, onLoading).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<AnimalWithDetails>>()
        val result = animalRepository.getAnimals()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isNotEmpty()
    }
}