package com.example.petfinderremake.features.discover.domain.usecase.request

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.onError
import com.example.petfinderremake.features.discover.AnimalRepositoryTest
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

class RequestDiscoverPageUseCaseTest : AnimalRepositoryTest() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockAnimalRepository: AnimalRepository

    @MockK
    lateinit var httpException: HttpException

    @MockK(relaxed = true)
    private var mockOnLoading: (Boolean) -> Unit = {}

    private val onLoading: (Boolean) -> Unit = {}

    private val notInitDiscoverPage = PaginatedAnimals.initPaginatedAnimals.copy(
        listOf(
            AnimalWithDetails.noAnimalWithDetails
        )
    )

    private lateinit var requestDiscoverPageUseCase: RequestDiscoverPageUseCase
    private lateinit var mockRequestDiscoverPageUseCase: RequestDiscoverPageUseCase

    @Before
    fun setup() {
        requestDiscoverPageUseCase = RequestDiscoverPageUseCase(animalRepository)
        mockRequestDiscoverPageUseCase = RequestDiscoverPageUseCase(mockAnimalRepository)
    }

    @Test
    fun `when request discover page, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = requestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when request discover page without any exceptions, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = requestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when request discover page without any exceptions, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = requestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }

    @Test
    fun `when request discover page with network exceptions, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request discover page with network exceptions, then result of Error is type of NetworkError `() {
        runBlocking {

            var error: RootError? = null

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(NetworkError::class.java)
        }
    }

    @Test
    fun `when request discover page with network exceptions, then onLoading returns false `() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            mockRequestDiscoverPageUseCase(mockOnLoading)

            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }
        }
    }

    @Test
    fun `when request discover page with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))

        }
    }

    @Test
    fun `when request discover page with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))

        }
    }

    @Test
    fun `when request discover page with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.NOT_FOUND))

        }
    }

    @Test
    fun `when request discover page with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.UNEXPECTED_ERROR))

        }
    }

    @Test
    fun `when request discover page with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
            coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.MISSING_PARAMETERS))

        }
    }

    @Test
    fun `when request discover page with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        runBlocking {

            var handledException: Exception? = null

            //when
            try {
                every { httpException.code() }.returns(0)
                coEvery { mockAnimalRepository.requestDiscoverPage() }.throws(httpException)
                mockRequestDiscoverPageUseCase(onLoading)
            } catch (e: Exception) {
                handledException = e
            }

            //then
            Truth.assertThat(handledException)
                .isInstanceOf(NetworkError.NetworkErrorTypeException::class.java)

        }
    }

    @Test
    fun `when request discover page with storage exceptions, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(PaginatedAnimals.initPaginatedAnimals)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request discover page with storage exceptions, then result of Error is type of StorageError`() {
        runBlocking {

            var error: RootError? = null

            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(PaginatedAnimals.initPaginatedAnimals)
            val result = mockRequestDiscoverPageUseCase(onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(StorageError::class.java)

        }
    }

    @Test
    fun `when request discover page with storage exceptions, then StorageError is NO_DATA_TO_STORE`() {
        runBlocking {


            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(PaginatedAnimals.initPaginatedAnimals)
            val result = mockRequestDiscoverPageUseCase(onLoading)

            //then
            Truth.assertThat(result).isEqualTo(Result.Error(StorageError.NO_DATA_TO_STORE))

        }
    }

    @Test
    fun `when request discover page with storage exceptions, then onLoading returns false`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(PaginatedAnimals.initPaginatedAnimals)
            mockRequestDiscoverPageUseCase(mockOnLoading)


            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request discover page without  exceptions, then store discover page and loading are executed in order`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestDiscoverPage() }.returns(notInitDiscoverPage)
            coEvery { mockAnimalRepository.storeDiscoverPaginatedAnimals(notInitDiscoverPage) }.returns(
                Unit
            )
            mockRequestDiscoverPageUseCase(mockOnLoading)


            //then
            coVerifyOrder {
                mockAnimalRepository.storeDiscoverPaginatedAnimals(notInitDiscoverPage)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request discover page, then discover page is stored`() {
        runBlocking {

            //when
            requestDiscoverPageUseCase(onLoading)

            //then
            val result = animalRepository.getDiscoverPaginatedAnimals().first()
            Truth.assertThat(result).isEqualTo(notInitDiscoverPage)
        }
    }
}