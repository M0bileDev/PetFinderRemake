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
        runBlocking {

            //when
            val result = requestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when request single animal  without any exceptions, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = requestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when request single animal  without any exceptions, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = requestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))
        }
    }

    @Test
    fun `when request single animal  with network exceptions, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request single animal  with network exceptions, then result of Error is type of NetworkError `() {
        runBlocking {

            var error: RootError? = null

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(NetworkError::class.java)
        }
    }

    @Test
    fun `when request single animal  with network exceptions, then onLoading returns false `() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            mockRequestSingleAnimalUseCase(id, mockOnLoading)

            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }
        }
    }

    @Test
    fun `when request single animal  with ACCESS_DENIED_INVALID_CREDENTIALS exceptions, then result of use case is equal to ACCESS_DENIED_INVALID_CREDENTIALS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS))

        }
    }

    @Test
    fun `when request single animal  with ACCESS_DENIED_INSUFFICIENT_ACCESS exceptions, then result of use case is equal to ACCESS_DENIED_INSUFFICIENT_ACCESS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS))

        }
    }

    @Test
    fun `when request single animal  with NOT_FOUND exceptions, then result of use case is equal to NOT_FOUND`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.NOT_FOUND.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.NOT_FOUND))

        }
    }

    @Test
    fun `when request single animal  with UNEXPECTED_ERROR exceptions, then result of use case is equal to UNEXPECTED_ERROR`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.UNEXPECTED_ERROR.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.UNEXPECTED_ERROR))

        }
    }

    @Test
    fun `when request single animal  with MISSING_PARAMETERS exceptions, then result of use case is equal to MISSING_PARAMETERS`() {
        runBlocking {

            //when
            every { httpException.code() }.returns(NetworkError.MISSING_PARAMETERS.code)
            coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
            val result = mockRequestSingleAnimalUseCase(id, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(NetworkError.MISSING_PARAMETERS))

        }
    }

    @Test
    fun `when request single animal  with unsupported exceptions, then thrown exception is instance of NetworkErrorTypeException`() {
        runBlocking {

            var handledException: Exception? = null

            //when
            try {
                every { httpException.code() }.returns(0)
                coEvery { mockAnimalRepository.requestAnimal(id) }.throws(httpException)
                mockRequestSingleAnimalUseCase(id, onLoading)
            } catch (e: Exception) {
                handledException = e
            }

            //then
            Truth.assertThat(handledException)
                .isInstanceOf(NetworkError.NetworkErrorTypeException::class.java)

        }
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then result of use case is instance of Result Error type`() {
        runBlocking {

            //when
            val result = mockRequestSingleAnimalUseCase(negativeId, onLoading)

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then result of Error is type of ArgumentError`() {
        runBlocking {

            var error: RootError? = null

            //when
            val result = mockRequestSingleAnimalUseCase(negativeId, onLoading)
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(ArgumentError::class.java)

        }
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then ArgumentError is ARGUMENT_IS_EMPTY`() {
        runBlocking {


            //when
            val result = mockRequestSingleAnimalUseCase(negativeId, onLoading)

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(ArgumentError.ARGUMENT_IS_NEGATIVE))

        }
    }

    @Test
    fun `when request single animal with negative numeric argument exception, then onLoading returns false`() {
        runBlocking {

            //when
            mockRequestSingleAnimalUseCase(negativeId, mockOnLoading)


            //then
            coVerifySequence {
                mockOnLoading(true)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request single animal without exceptions, then store animals and loading are executed in order`() {
        runBlocking {

            //when
            coEvery { mockAnimalRepository.requestAnimal(id) }.returns(animalWithDetails)
            coEvery { mockAnimalRepository.storeAnimals(storeAnimal) }.returns(Unit)
            mockRequestSingleAnimalUseCase(id, mockOnLoading)


            //then
            coVerifyOrder {
                mockAnimalRepository.storeAnimals(storeAnimal)
                mockOnLoading(false)
            }

        }
    }

    @Test
    fun `when request single animal, then animal is stored`() {
        runBlocking {

            //when
            requestSingleAnimalUseCase(id, onLoading)

            //then
            val result = animalRepository.getAnimals().first()
            Truth.assertThat(result).isNotEmpty()
        }
    }
}