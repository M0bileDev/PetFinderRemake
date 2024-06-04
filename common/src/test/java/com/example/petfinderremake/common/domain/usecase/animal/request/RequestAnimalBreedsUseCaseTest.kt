package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.onError
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
        mockRequestAnimalBreedsUseCase = RequestAnimalBreedsUseCase(mockAnimalRepository)
    }

    @Test
    fun `when request animal breeds, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = requestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when request animal breeds without any exceptions, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = requestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when request animal breeds without any exceptions, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = requestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }

    @Test
    fun `when request animal breeds with network exceptions, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request animal breeds with network exceptions, then result of Error is type of NetworkError `() {
        runBlocking {

            var error: RootError? = null

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(NetworkError::class.java)
        }
    }

    @Test
    fun `when request animal breeds with network exceptions, then onLoading returns false `() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            mockRequestAnimalBreedsUseCase(type, mockOnLoading)

            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }
        }
    }

    @Test
    fun `when request animal breeds with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))

        }
    }

    @Test
    fun `when request animal breeds with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))

        }
    }

    @Test
    fun `when request animal breeds with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.NOT_FOUND))

        }
    }

    @Test
    fun `when request animal breeds with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.UNEXPECTED_ERROR))

        }
    }

    @Test
    fun `when request animal breeds with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.MISSING_PARAMETERS))

        }
    }

    @Test
    fun `when request animal breeds with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        runBlocking {

            var handledException: Exception? = null

            //when
            try {
                every { httpException.code() }.returns(0)
                coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.throws(httpException)
                mockRequestAnimalBreedsUseCase(type, onLoading)
            } catch (e: Exception) {
                handledException = e
            }

            //then
            Truth.assertThat(handledException)
                .isInstanceOf(NetworkError.NetworkErrorTypeException::class.java)

        }
    }

    @Test
    fun `when request animal breeds with storage exceptions, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.returns(emptyList())
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request animal breeds with storage exceptions, then result of Error is type of StorageError`() {
        runBlocking {

            var error: RootError? = null

            //when
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.returns(emptyList())
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(StorageError::class.java)

        }
    }

    @Test
    fun `when request animal breeds with storage exceptions, then StorageError is NO_DATA_TO_STORE`() {
        runBlocking {


            //when
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.returns(emptyList())
            val result = mockRequestAnimalBreedsUseCase(type, onLoading)

            //then
            Truth.assertThat(result).isEqualTo(Result.Error(StorageError.NO_DATA_TO_STORE))

        }
    }

    @Test
    fun `when request animal breeds with storage exceptions, then onLoading returns false`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.returns(emptyList())
            mockRequestAnimalBreedsUseCase(type, mockOnLoading)


            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request animal breeds without  exceptions, then delete breeds, store animals and loading are executed in order`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.deleteBreeds() }.returns(Unit)
            coEvery { mockAnimalRepository.storeAnimalBreeds(animalBreeds) }.returns(Unit)
            coEvery { mockAnimalRepository.requestAnimalBreeds(type) }.returns(animalBreeds)
            mockRequestAnimalBreedsUseCase(type, mockOnLoading)


            //then
            coVerifyOrder {
                mockAnimalRepository.deleteBreeds()
                mockAnimalRepository.storeAnimalBreeds(animalBreeds)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request animal breeds, then breeds are stored`() {
        runBlocking {

            //when
            requestAnimalBreedsUseCase(type, onLoading)

            //then
            val result = animalRepository.getAnimalBreeds().first()
            Truth.assertThat(result).isNotEmpty()
        }
    }

}