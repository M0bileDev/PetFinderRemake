package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.onError
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAnimalTypeUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalTypeUseCase: GetAnimalTypeUseCase
    private val type = "Type1"
    private val emptyType = ""
    private val animalType = AnimalType(emptyList(), emptyList(), emptyList(), type)
    private val types = listOf(animalType)

    @Before
    fun setup() {
        getAnimalTypeUseCase = GetAnimalTypeUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimalTypes(types)
        }
    }

    @Test
    fun `when get type, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getAnimalTypeUseCase(type).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get type, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getAnimalTypeUseCase(type).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get type with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getAnimalTypeUseCase(type).first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(animalType))

        }
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is  instance of Result Error `() {
        runBlocking {

            //when
            val result = getAnimalTypeUseCase(emptyType).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Error::class.java)

        }
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is instance of Error ArgumentError`() {
        runBlocking {

            var error: RootError? = null

            //when
            val result = getAnimalTypeUseCase(emptyType).first()
            result.onError { error = it.error }

            //then
            Truth.assertThat(error).isInstanceOf(ArgumentError::class.java)

        }
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is Error ArgumentError ARGUMENT_IS_EMPTY`() {
        runBlocking {

            //when
            val result = getAnimalTypeUseCase(emptyType).first()

            //then
            Truth.assertThat(result)
                .isEqualTo(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY))

        }
    }
}