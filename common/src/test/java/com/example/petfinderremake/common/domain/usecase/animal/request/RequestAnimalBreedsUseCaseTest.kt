package com.example.petfinderremake.common.domain.usecase.animal.request

import android.annotation.SuppressLint
import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.onError
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.spyk
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class RequestAnimalBreedsUseCaseTest : AnimalRepositoryTest() {

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
    private val animalBreeds = listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"))

    private lateinit var requestAnimalBreedsUseCase: RequestAnimalBreedsUseCase
    private lateinit var mockRequestAnimalBreedsUseCase: RequestAnimalBreedsUseCase

    @Before
    fun setup() {
        requestAnimalBreedsUseCase = RequestAnimalBreedsUseCase(animalRepository)
        mockRequestAnimalBreedsUseCase = spyk(RequestAnimalBreedsUseCase(mockAnimalRepository))
    }

    @Test
    fun `when request animal breeds, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalBreedsUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when request animal breeds without any exceptions, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalBreedsUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)

    }

    @Test
    fun `when request animal breeds without any exceptions, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = requestAnimalBreedsUseCase(type, onLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun `when request animal breeds with network exceptions, then result of use case is instance of Result Error type`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request animal breeds with network exceptions, then result of Error is type of NetworkError `() {
        var sut: RootError? = null

        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(NetworkError::class.java)
    }

    @Test
    fun `when request animal breeds with network exceptions, then onLoading returns false `() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        verifyOrder {
            mockOnLoading(true)
            mockOnLoading(false)
        }
    }

    @Test
    fun `when request animal breeds with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))
    }

    @Test
    fun `when request animal breeds with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        //when
        every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))
    }

    @Test
    fun `when request animal breeds with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        //when
        every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.NOT_FOUND))
    }

    @Test
    fun `when request animal breeds with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        //when
        every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.UNEXPECTED_ERROR))
    }

    @Test
    fun `when request animal breeds with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        //when
        every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertValue(Result.Error(NetworkError.MISSING_PARAMETERS))
    }

    @Test
    fun `when request animal breeds with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        every { httpException.code() }.returns(501)
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.error(
                httpException
            )
        )

        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
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
    fun `when request animal breeds with storage exceptions, then result of use case is instance of Result Error type`() {
        //when
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.just(
                emptyList()
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when request animal breeds with storage exceptions, then result of Error is type of StorageError`() {
        var sut: RootError? = null

        //when
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.just(
                emptyList()
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.values().first().onError { sut = it.error }
        Truth.assertThat(sut).isInstanceOf(StorageError::class.java)
    }

    @Test
    fun `when request animal breeds with storage exceptions, then StorageError is NO_DATA_TO_STORE`() {

        //when
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.just(
                emptyList()
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)

        //then
        testObserver.assertResult(Result.Error(StorageError.NO_DATA_TO_STORE))
    }

    @SuppressLint("CheckResult")
    @Test
    fun `when request animal breeds with storage exceptions, then onLoading returns false`() {
        //when
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.just(
                emptyList()
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        mockRequestAnimalBreedsUseCase(type, mockOnLoading).subscribe(testObserver)

        //then
        verifyOrder {
            mockOnLoading(true)
            mockOnLoading(false)
        }
    }

    @SuppressLint("CheckResult")
    @Test
    fun `when request animal breeds without  exceptions, then delete breeds, store animals and loading are executed in order`() {
        //when
        every { mockAnimalRepository.deleteBreeds() }.returns(Completable.complete())
        every { mockAnimalRepository.storeAnimalBreeds(animalBreeds) }.returns(Completable.complete())
        every { mockAnimalRepository.requestAnimalBreeds(type) }.returns(
            Observable.just(
                animalBreeds
            )
        )
        val testObserver = TestObserver<Result<Unit, RootError>>()
        val result = mockRequestAnimalBreedsUseCase(type, mockOnLoading)
        result.subscribe(testObserver)


        //then
        verifyOrder {
            mockAnimalRepository.deleteBreeds()
            mockAnimalRepository.storeAnimalBreeds(animalBreeds)
            mockOnLoading(false)
        }
    }

    @Test
    fun `when request animal breeds, then breeds are stored`() {
        //when
        val testObserver = TestObserver<Result<Unit, RootError>>()
        requestAnimalBreedsUseCase(type, onLoading).subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<AnimalBreeds>>()
        val result = animalRepository.getAnimalBreeds()
        result.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isNotEmpty()
    }

}